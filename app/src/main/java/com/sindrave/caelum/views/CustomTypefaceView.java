package com.sindrave.caelum.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.sindrave.caelum.R;

/**
 * Created by Yanik on 16/09/2014.
 */
public class CustomTypefaceView extends TypefaceView {

    public CustomTypefaceView(Context context) {
        super(context);
    }

    public CustomTypefaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomTypeface(context, attrs);
    }

    public CustomTypefaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomTypeface(context, attrs);
    }

    private void setCustomTypeface(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTypefaceView);
        String customFontString = typedArray.getString(R.styleable.CustomTypefaceView_customTypeface);
        setCustomTypeface(context, customFontString);
        typedArray.recycle();
    }

}
