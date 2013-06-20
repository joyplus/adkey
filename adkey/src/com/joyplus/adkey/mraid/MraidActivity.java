package com.joyplus.adkey.mraid;

import com.joyplus.adkey.mraid.MraidView.ExpansionStyle;
import com.joyplus.adkey.mraid.MraidView.NativeCloseButtonStyle;
import com.joyplus.adkey.mraid.MraidView.PlacementType;

import android.view.View;

public class MraidActivity extends BaseActivity {    
    private MraidView mAdView;
    
    @Override
    public View getAdView() {
        mAdView = new MraidView(this, ExpansionStyle.DISABLED, NativeCloseButtonStyle.AD_CONTROLLED,
                PlacementType.INTERSTITIAL);
        
        mAdView.setOnReadyListener(new MraidView.OnReadyListener() {
           public void onReady(MraidView view) {
               showInterstitialCloseButton();
           }
        });
        
        mAdView.setOnCloseButtonStateChange(new MraidView.OnCloseButtonStateChangeListener() {
            public void onCloseButtonStateChange(MraidView view, boolean enabled) {
                if (enabled) showInterstitialCloseButton();
                else hideInterstitialCloseButton();
            }
        });
        
        mAdView.setOnCloseListener(new MraidView.OnCloseListener() {
            public void onClose(MraidView view, MraidView.ViewState newViewState) {
                finish();
            }
        });
        
        String source = getIntent().getStringExtra("com.adsdk.sdk.mraid.Source");
        mAdView.loadHtmlData(source);
        
        return mAdView;
    }
    
    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
}
