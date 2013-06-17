package com.joyplus.adkey.video;

import static com.joyplus.adkey.Const.TAG;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.net.Uri;
import android.os.ConditionVariable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController.MediaPlayerControl;

public class SDKVideoView extends SurfaceView implements MediaPlayerControl {
	private Uri mUri;
	private int mDuration;

	private static final int STATE_ERROR = -1;
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREPARING = 1;
	private static final int STATE_PREPARED = 2;
	private static final int STATE_PLAYING = 3;
	private static final int STATE_PAUSED = 4;
	private static final int STATE_PLAYBACK_COMPLETED = 5;

	private int mCurrentState = STATE_IDLE;
	private int mTargetState = STATE_IDLE;

	private MediaPlayer mMediaPlayer = null;
	private int mWidth;
	private int mHeight;
	private int mVideoWidth;
	private int mVideoHeight;
	private int mSurfaceWidth;
	private int mSurfaceHeight;
	private int mDisplayMode;
	private MediaController mMediaController;
	private OnCompletionListener mOnCompletionListener;
	private OnStartListener mOnStartListener;
	private MediaPlayer.OnPreparedListener mOnPreparedListener;
	private int mCurrentBufferPercentage;
	private OnErrorListener mOnErrorListener;
	private OnInfoListener mOnInfoListener;
	private int mSeekWhenPrepared;
	private boolean mPlayWhenSurfaceReady;
	private boolean mSurfaceReady = false;
	private Context mContext;
	private Thread mTimeEventThread;
	private Runnable mTimeEventRunnable;
	private ConditionVariable mTimeEventThreadDone = new ConditionVariable(
			false);
	private HashMap<Integer, Vector<OnTimeEventListener>> mTimeEventListeners = new HashMap<Integer, Vector<OnTimeEventListener>>();
	public Handler mHandler;

	public SDKVideoView(Context context, int width, int height,
			int displayMode) {
		super(context);
		this.mContext = context;
		mWidth = width;
		mHeight = height;
		mDisplayMode = displayMode;
		initVideoView();
	}

	public void destroy(){
		mTimeEventThreadDone.open();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
		int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
		if (mVideoWidth > 0 && mVideoHeight > 0) {
			if ( mVideoWidth * height  > width * mVideoHeight ) {

				height = width * mVideoHeight / mVideoWidth;
			} else if ( mVideoWidth * height  < width * mVideoHeight ) {

				width = height * mVideoWidth / mVideoHeight;
			} else {

			}
		}

		setMeasuredDimension(width, height);
	}

	private void initVideoView() {
		mHandler = new Handler();
		mVideoWidth = 0;
		mVideoHeight = 0;
		mSurfaceWidth = 0;
		mSurfaceHeight = 0;
		mSurfaceReady = false;
		setVisibility(View.VISIBLE);
		getHolder().addCallback(mSHCallback);
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		mCurrentState = STATE_IDLE;
		mTargetState = STATE_IDLE;
	}

	public void setVideoPath(String path) {
		setVideoURI(Uri.parse(path));
	}

	public void setVideoURI(Uri uri) {
		mUri = uri;
		mSeekWhenPrepared = 0;
		openVideo();
	}

	public void stopPlayback() {
		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			mTargetState = STATE_IDLE;
		}
	}

	private void openVideo() {
		if (mUri == null) {
			return;
		}
		mPlayWhenSurfaceReady = false;
		if (!mSurfaceReady) {
			mPlayWhenSurfaceReady = true;
			return;
		}
		release(false);
		try {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDisplay(getHolder());
			mMediaPlayer.setOnPreparedListener(mPreparedListener);
			mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
			mDuration = -1;
			mMediaPlayer.setOnCompletionListener(mCompletionListener);
			mMediaPlayer.setOnErrorListener(mErrorListener);
			mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
			mMediaPlayer.setOnInfoListener(mInfoListener);
			mCurrentBufferPercentage = 0;
			mMediaPlayer.setDataSource(mContext, mUri);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setScreenOnWhilePlaying(true);
			mMediaPlayer.prepareAsync();
			mTimeEventRunnable = new Runnable() {
				@Override
				public void run() {
					do {
						if ((mMediaPlayer != null)
								&& (mCurrentState == STATE_PLAYING)) {
							try {
								final int time = mMediaPlayer
										.getCurrentPosition() / 1000;
								Vector<OnTimeEventListener> listeners = mTimeEventListeners
										.get(time);
								if (listeners != null) {
									for (int i = 0; i < listeners.size(); i++) {
										final OnTimeEventListener l = listeners
												.elementAt(i);
										mHandler.post(new Runnable() {

											@Override
											public void run() {
												l.onTimeEvent(time);
											}
										});
									}
									listeners.clear();
								}
							} catch (Exception e) {
							}
						}
					} while (!mTimeEventThreadDone.block(1000));
				}
			};
			mTimeEventThread = new Thread(mTimeEventRunnable);
			mTimeEventThread.start();
			mCurrentState = STATE_PREPARING;
			attachMediaController();
		} catch (IOException ex) {
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,
					MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		} catch (IllegalArgumentException ex) {
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,
					MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		}
	}

	public void setMediaController(MediaController controller) {
		if (mMediaController != null) {
			mMediaController.hide();
		}
		mMediaController = controller;
		attachMediaController();
	}

	private void attachMediaController() {
		if (mMediaPlayer != null && mMediaController != null) {
			mMediaController.setMediaPlayer(this);
			mMediaController.setEnabled(isInPlaybackState());
		}
	}

	private void setVideoDisplaySize() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		if (mMediaPlayer != null) {
			mVideoWidth = mMediaPlayer.getVideoWidth();
			mVideoHeight = mMediaPlayer.getVideoHeight();
		}
		if ((mSurfaceReady) && (mVideoWidth > 0 && mVideoHeight > 0)) {
			if (mDisplayMode == VideoData.DISPLAY_NORMAL) {
				if (mVideoWidth * mHeight > mWidth * mVideoHeight) {

					mHeight = mWidth * mVideoHeight / mVideoWidth;
				} else if (mVideoWidth * mHeight < mWidth * mVideoHeight) {

					mWidth = mHeight * mVideoWidth / mVideoHeight;
				}
			}
			getHolder().setFixedSize(mWidth, mHeight);
		}
		getHolder().setFixedSize(mVideoWidth, mVideoHeight);
	}

	MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
		@Override
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		}
	};

	MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(MediaPlayer mp) {
			mCurrentState = STATE_PREPARED;

			if (mOnPreparedListener != null) {
				mOnPreparedListener.onPrepared(mMediaPlayer);
			}
			if (mMediaController != null) {
				mMediaController.setEnabled(true);
			}
			int seekToPosition = mSeekWhenPrepared;

			if (seekToPosition != 0) {
				seekTo(seekToPosition);
			}
			if (!mSurfaceReady) {
				return;
			}
			setVideoDisplaySize();
			if (mTargetState == STATE_PLAYING) {
				start();
			} else if (!isPlaying()
					&& (seekToPosition != 0 || getCurrentPosition() > 0)) {
				if (mMediaController != null) {
					mMediaController.show(0);
				}
			}
		}
	};

	private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			mCurrentState = STATE_PLAYBACK_COMPLETED;
			mTargetState = STATE_PLAYBACK_COMPLETED;
			if (mMediaController != null) {
				mMediaController.show(0);
			}
			if (mOnCompletionListener != null) {
				mOnCompletionListener.onCompletion(mMediaPlayer);
			}
			/*
			 * play completion，update adv.mp4
			 */
			File file_temp = new File(Const.DOWNLOAD_PATH+Const.DOWNLOAD_READY_FILE);
			if(file_temp.exists())
			{
				File file = new File(Const.DOWNLOAD_PATH+Const.DOWNLoAD_PLAY_FILE+Util.ExternalName);
				if(file.exists())
				{
					file.delete();
				}
				file_temp.renameTo(file);
			}
		}
	};

	private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			if (mMediaController != null) {
				mMediaController.hide();
			}
			if (mOnErrorListener != null) {
				if (mOnErrorListener.onError(mMediaPlayer, framework_err,
						impl_err)) {
					return true;
				}
			}

			return true;
		}
	};

	private MediaPlayer.OnInfoListener mInfoListener = new MediaPlayer.OnInfoListener() {
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {

			if (mOnInfoListener != null) {
				if (mOnInfoListener.onInfo(mMediaPlayer, what,
						extra)) {
					return true;
				}
			}

			return true;
		}
	};

	private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			mCurrentBufferPercentage = percent;
		}
	};

	SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			mSurfaceWidth = w;
			mSurfaceHeight = h;
			setVideoDisplaySize();
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mSurfaceReady = true;
			if (mPlayWhenSurfaceReady) {
				openVideo();
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			mSurfaceReady = false;
			if (mMediaController != null) {
				mMediaController.hide();
			}
			release(true);
		}
	};

	private void release(boolean cleartargetstate) {
		if (mMediaPlayer != null) {
			mCurrentState = STATE_IDLE;
			if (mTimeEventThread != null) {
				mTimeEventThreadDone.open();
				mTimeEventThread = null;
			}
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
			if (cleartargetstate) {
				mTargetState = STATE_IDLE;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if ((isInPlaybackState()) && (mMediaController != null)
				&& (ev.getAction() == MotionEvent.ACTION_DOWN)) {
			toggleMediaControlsVisiblity();
		}
		return false;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent ev) {
		if (isInPlaybackState() && mMediaController != null) {
			toggleMediaControlsVisiblity();
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK
				&& keyCode != KeyEvent.KEYCODE_VOLUME_UP
				&& keyCode != KeyEvent.KEYCODE_VOLUME_DOWN
				&& keyCode != KeyEvent.KEYCODE_MENU
				&& keyCode != KeyEvent.KEYCODE_CALL
				&& keyCode != KeyEvent.KEYCODE_ENDCALL;
		if (isInPlaybackState() && isKeyCodeSupported
				&& mMediaController != null) {
			if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
					|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
				if (mMediaPlayer.isPlaying()) {
					pause();
				} else {
					start();
				}
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
					&& mMediaPlayer.isPlaying()) {
				pause();
			} else {
				toggleMediaControlsVisiblity();
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	private void toggleMediaControlsVisiblity() {
		if (mMediaController != null) {
			mMediaController.toggle();
		}
	}

	@Override
	public void start() {
		mTargetState = STATE_PLAYING;
		if (isInPlaybackState()) {
			Intent intent = new Intent("com.android.music.musicservicecommand");
			intent.putExtra("command", "pause");
			mContext.sendBroadcast(intent);
			mMediaPlayer.start();
			if (mMediaController != null) {
				mMediaController.onStart();
			}
			if (mCurrentState == STATE_PREPARED) {
				if (mOnStartListener != null) {
					mOnStartListener.onVideoStart();
				}
			}
			mCurrentState = STATE_PLAYING;
		} else if (mMediaPlayer == null) {
			openVideo();
		}
	}

	@Override
	public void pause() {
		if (isInPlaybackState()) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mCurrentState = STATE_PAUSED;
				if (mMediaController != null) {
					mMediaController.onPause();
				}
			}
		}
		mTargetState = STATE_PAUSED;
	}

	@Override
	public int getDuration() {
		if (isInPlaybackState()) {
			if (mDuration > 0) {
				return mDuration;
			}
			mDuration = mMediaPlayer.getDuration();
			return mDuration;
		}
		mDuration = -1;
		return mDuration;
	}

	@Override
	public int getCurrentPosition() {
		if (isInPlaybackState()) {
			return mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	@Override
	public void seekTo(int msec) {
		if (isInPlaybackState()) {
			mMediaPlayer.seekTo(msec);
			mSeekWhenPrepared = 0;
		} else {
			mSeekWhenPrepared = msec;
		}
	}

	@Override
	public boolean isPlaying() {
		return isInPlaybackState() && mMediaPlayer.isPlaying();
	}

	@Override
	public int getBufferPercentage() {
		if (mMediaPlayer != null) {
			return mCurrentBufferPercentage;
		}
		return 0;
	}

	private boolean isInPlaybackState() {
		return (mMediaPlayer != null && mCurrentState != STATE_ERROR
				&& mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
	}

	public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
		mOnPreparedListener = l;
	}

	public void setOnCompletionListener(OnCompletionListener l) {
		mOnCompletionListener = l;
	}

	public void setOnErrorListener(OnErrorListener l) {
		mOnErrorListener = l;
	}

	public void setOnInfoListener(OnInfoListener l) {
		mOnInfoListener = l;
	}

	public void setOnStartListener(OnStartListener l) {
		mOnStartListener = l;
	}

	public void setOnTimeEventListener(int time,
			OnTimeEventListener onTimeEventListener) {
		Vector<OnTimeEventListener> listeners = mTimeEventListeners.get(time);
		if (listeners == null) {
			listeners = new Vector<OnTimeEventListener>();
			mTimeEventListeners.put(time, listeners);
		}
		listeners.add(onTimeEventListener);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	public interface OnStartListener {
		public void onVideoStart();
	}

	public interface OnTimeEventListener {
		public void onTimeEvent(int time);
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return false;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

}
