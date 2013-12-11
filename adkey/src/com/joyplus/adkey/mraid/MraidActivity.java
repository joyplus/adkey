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
        source = "<div id=\"im_popupWindow_miniMsg\" style=\"background-color: #000000;bottom: 0;position: fixed;right: 0;width: 292px;height: 220px;display: block;text-align: center;\">"
       +"<table style=\"font-size:small\">"
       +"<tr>"
       +"<td>"
       +"<span style=\"color:#ffffff;\"> 广告词位置！ </span>"
       +"</td>"
+"<td style=\"width: 3%\">"
+"<a onclick=\"cofir()\">"
+"<img src=\"http://csdnimg.cn/www/images/popup/pic_close.gif\" alt=\"close\" />"
+"</a>"
+"</td>"
+"</tr>"
+"</table>"
+"<div >"
+"<a target=\"_blank\" href=\"http://events.csdn.net/SMI/main.html\">"
+"<img border=\"0\" title=\"\" style=\" border:5 5 5 5\" alt=\"\" src=\"http://info-database.csdn.net/Upload/2010-11-03/280X188(4).jpg\" />"
+"</a>"
+"</div>"
+"</div>"

 

+"<script type=\"text/javascript\">"

 +"function cofir() {"
 +"        document.getElementById(\"im_popupWindow_miniMsg\").style.display = \"none\";"

 +"   }"

+"</script>\";";
       mAdView.loadHtmlData(source);
        
        return mAdView;
    }
    
    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
}
