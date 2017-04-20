package com.xzkeji.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xzkeji.utils.LogUtil;

/**
 * Created by xianguo on 19/4/17.
 * activity 基类
 */
@SuppressWarnings("unchecked")
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(getClass().getSimpleName());
    }

    public <T extends View>T $(@IdRes int resId){
        return (T) findViewById(resId);
    }
}
