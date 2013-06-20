package com.joyplus.adkey.mraid;


import com.joyplus.adkey.Const;
import com.joyplus.adkey.banner.InAppWebView;
import com.joyplus.adkey.widget.Log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

class MraidBrowserController extends MraidAbstractController {
    private static final String LOGTAG = "MraidBrowserController";
    
    MraidBrowserController(MraidView view) {
        super(view);
    }
    
    protected void open(String url) {
        Log.d(LOGTAG, "Opening in-app browser: " + url);
        
        MraidView view = getView();
        if (view.getOnOpenListener() != null) {
            view.getOnOpenListener().onOpen(view);
        }
        
        Context context = getView().getContext();
        if(url.endsWith(".mp4")){
            Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.parse(url), "video/mp4");
            context.startActivity(i); //warning no error handling will cause force close if no media player on phone.
       }
        else{
        	Intent i = new Intent(Intent.ACTION_VIEW);
        	i.setData(Uri.parse(url));
        	context.startActivity(i);
        }
    }
}
