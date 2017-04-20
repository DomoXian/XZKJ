package com.xzkeji.view;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.export.external.interfaces.IX5WebSettings;
import com.tencent.smtt.sdk.WebSettings;

/**
 * Created by xianguo on 19/4/17.
 * 网页视图
 */

public class WebView extends com.tencent.smtt.sdk.WebView {
    public WebView(Context context) {
        this(context, null);
    }

    public WebView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    private void initViews() {

    }
}
