package com.sindrave.caelum.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Yanik on 16/09/2014.
 */
public abstract class TypefaceView extends TextView{
    public static final String ASSETS_FONTS_PATH = "fonts/";

    public TypefaceView(Context context) {
        super(context);
    }

    public TypefaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TypefaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean setCustomTypeface(Context context, String fontName){
        // TODO: try other directories within assets folder?
        String customFontPath = ASSETS_FONTS_PATH + fontName;
        try {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), customFontPath);
            setTypeface(typeface);
            return true;
        } catch (Exception e) {
            Log.e(CustomTypefaceView.class.getName(), "Error trying to get asset: " + customFontPath);
            return false;
        }
    }
}
