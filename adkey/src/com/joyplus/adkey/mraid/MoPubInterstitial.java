/*
 * Copyright (c) 2011, MoPub Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'MoPub Inc.' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.joyplus.adkey.mraid;

import java.util.HashMap;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.mraid.BaseInterstitialAdapter.BaseInterstitialAdapterListener;
import com.joyplus.adkey.mraid.MoPubView.LocationAwareness;
import com.joyplus.adkey.widget.Log;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;


public class MoPubInterstitial implements AdListener {
    
    private enum InterstitialState { HTML_AD_READY, NATIVE_AD_READY, NOT_READY };
    
    private MoPubInterstitialView mInterstitialView;
    private BaseInterstitialAdapter mInterstitialAdapter;
    private MoPubInterstitialListener mListener;
    private Activity mActivity;
    private String mAdUnitId;
    private BaseInterstitialAdapterListener mAdapterListener;
    private DefaultInterstitialAdapterListener mDefaultAdapterListener;
    private InterstitialState mCurrentInterstitialState;
    
    public interface MoPubInterstitialListener {
        public void OnInterstitialLoaded();
        public void OnInterstitialFailed();
    }
    
    public MoPubInterstitial(Activity activity, String id) {
        mActivity = activity;
        mAdUnitId = id;
        
        mInterstitialView = new MoPubInterstitialView(mActivity);
        mInterstitialView.setAdUnitId(mAdUnitId);
        
        mCurrentInterstitialState = InterstitialState.NOT_READY;
        mDefaultAdapterListener = new DefaultInterstitialAdapterListener();
        mAdapterListener = mDefaultAdapterListener;
    }

    public void load() {
    	invalidateCurrentInterstitial();	
    	mInterstitialView.loadAd();
    }

    public void forceRefresh() {
    	invalidateCurrentInterstitial();
    	mInterstitialView.forceRefresh();
    }

    private void invalidateCurrentInterstitial() {
        mCurrentInterstitialState = InterstitialState.NOT_READY;
        
        if (mInterstitialAdapter != null) {
            mInterstitialAdapter.invalidate();
            mInterstitialAdapter = null;
        }
        
        mAdapterListener = mDefaultAdapterListener;
        
        mInterstitialView.setAdListener(this);
        mInterstitialView.loadAd();
    }
    
    public boolean isReady() {
        return (mCurrentInterstitialState == InterstitialState.HTML_AD_READY) || 
                (mCurrentInterstitialState == InterstitialState.NATIVE_AD_READY);
    }
    
    public boolean show() {
        switch (mCurrentInterstitialState) {
//            case HTML_AD_READY: showHtmlInterstitial(); return true;
            case NATIVE_AD_READY: showNativeInterstitial(); return true;
            default: return false;
        }
    }
    
//    private void showHtmlInterstitial() {
//        String responseString = mInterstitialView.getResponseString();
//        Intent i = new Intent(mActivity, MoPubActivity.class);
//        i.putExtra("com.adsdk.sdk.mraid.AdUnitId", mAdUnitId);
//        i.putExtra("com.adsdk.sdk.mraid.Keywords", mInterstitialView.getKeywords());
//        i.putExtra("com.adsdk.sdk.mraid.Source", responseString);
//        i.putExtra("com.adsdk.sdk.mraid.ClickthroughUrl", mInterstitialView.getClickthroughUrl());
//        mActivity.startActivity(i);
//    }
    
    private void showNativeInterstitial() {
        if (mInterstitialAdapter != null) mInterstitialAdapter.showInterstitial();
    }
    
    public void OnAdFailed(MoPubView m) {
        mCurrentInterstitialState = InterstitialState.NOT_READY;
        if (mListener != null) mListener.OnInterstitialFailed();
    }

    public void OnAdLoaded(MoPubView m) {
        mCurrentInterstitialState = InterstitialState.HTML_AD_READY;
        
        if (mInterstitialAdapter != null) {
            mInterstitialAdapter.invalidate();
            mInterstitialAdapter = null;
        }
        
        if (mListener != null) mListener.OnInterstitialLoaded();
    }
    
    public void customEventDidLoadAd() {
    }

    public void customEventDidFailToLoadAd() {
        if (mInterstitialView != null) mInterstitialView.loadFailUrl();
    }

    public void customEventActionWillBegin() {
        if (mInterstitialView != null) mInterstitialView.registerClick();
    }

    @Deprecated
    public void showAd() {
        /* 
         * To show the ad immediately upon loading, we need to register a new OnAdLoadedListener,
         * as well as a new interstitial adapter listener.
         */
        
        mAdapterListener = new DefaultInterstitialAdapterListener() {
            public void onNativeInterstitialLoaded(BaseInterstitialAdapter adapter) {
                super.onNativeInterstitialLoaded(adapter);
                MoPubInterstitial.this.show();
            }
        };
        
//        mInterstitialView.setAdListener(new AdListener() {
//            public void OnAdLoaded(MoPubView m) {
//                MoPubInterstitial.this.OnAdLoaded(m);
//                MoPubInterstitial.this.show();
//            }
//        });
        
        mInterstitialView.loadAd();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    public void setKeywords(String keywords) {
        if (mInterstitialView != null) mInterstitialView.setKeywords(keywords);
    }

    public String getKeywords() {
        return (mInterstitialView != null) ? mInterstitialView.getKeywords() : null;
    }
    
    public Activity getActivity() {
    	return mActivity;
    }
    
    public void setListener(MoPubInterstitialListener listener) {
        mListener = listener;
    }
    
    public MoPubInterstitialListener getListener() {
        return mListener;
    }
    
    public Location getLocation() {
        return mInterstitialView.getLocation();
    }
    
    public void destroy() {
        mAdapterListener = null;
        
        if (mInterstitialAdapter != null) {
            mInterstitialAdapter.invalidate();
            mInterstitialAdapter = null;
        }
        
        mInterstitialView.setAdListener(null);
        mInterstitialView.destroy();
    }
    
    public void setLocationAwareness(LocationAwareness awareness) {
        mInterstitialView.setLocationAwareness(awareness);
    }

    public LocationAwareness getLocationAwareness() {
        return mInterstitialView.getLocationAwareness();
    }

    public void setLocationPrecision(int precision) {
        mInterstitialView.setLocationPrecision(precision);
    }

    public int getLocationPrecision() {
        return mInterstitialView.getLocationPrecision();
    }
    
    protected BaseInterstitialAdapterListener getInterstitialAdapterListener () {
        return mAdapterListener;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    public class DefaultInterstitialAdapterListener implements BaseInterstitialAdapterListener {
        public void onNativeInterstitialLoaded(BaseInterstitialAdapter adapter) {
            mCurrentInterstitialState = InterstitialState.NATIVE_AD_READY;
            if (mListener != null) mListener.OnInterstitialLoaded();
        }

        public void onNativeInterstitialFailed(BaseInterstitialAdapter adapter) {
            mCurrentInterstitialState = InterstitialState.NOT_READY;
            mInterstitialView.loadFailUrl();
        }

        public void onNativeInterstitialClicked(BaseInterstitialAdapter adapter) {
            mInterstitialView.registerClick();
        }
        
        public void onNativeInterstitialExpired(BaseInterstitialAdapter adapter) {
            mCurrentInterstitialState = InterstitialState.NOT_READY;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    public class MoPubInterstitialView extends MoPubView {
        
        public MoPubInterstitialView(Context context) {
            super(context);
            setAutorefreshEnabled(false);
        }
        
        @Override
        protected void loadNativeSDK(HashMap<String, String> paramsHash) {
            if (paramsHash == null) return;
            
            MoPubInterstitial interstitial = MoPubInterstitial.this;
            BaseInterstitialAdapterListener adapterListener = 
                interstitial.getInterstitialAdapterListener();
            String type = paramsHash.get("X-Adtype");
            
            if (type != null && (type.equals("interstitial") || type.equals("mraid"))) {
                String interstitialType = type.equals("interstitial") ? 
                        paramsHash.get("X-Fulladtype") : "mraid";
                
                Log.i("MoPub", "Loading native adapter for interstitial type: " + interstitialType);
                mInterstitialAdapter =
                        BaseInterstitialAdapter.getAdapterForType(interstitialType);
                
                if (mInterstitialAdapter != null) {
                    String jsonParams = paramsHash.get("X-Nativeparams");
                    mInterstitialAdapter.init(interstitial, jsonParams);
                    mInterstitialAdapter.setAdapterListener(adapterListener);
                    mInterstitialAdapter.loadInterstitial();
                    return;
                }
            }
            
            Log.i("MoPub", "Couldn't load native adapter. Trying next ad...");
            adapterListener.onNativeInterstitialFailed(null);
        }
        
    }

	@Override
	public void adClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adClosed(Ad ad, boolean completed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adLoadSucceeded(Ad ad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adShown(Ad ad, boolean succeeded) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void noAdFound() {
		// TODO Auto-generated method stub
		
	}
}
