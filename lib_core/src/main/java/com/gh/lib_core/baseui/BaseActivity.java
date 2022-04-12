package com.gh.lib_core.baseui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: gh
 * @description:
 * @date: 2020/8/3.
 * @from:
 */
public class BaseActivity extends AppCompatActivity {

    protected Activity activity;
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
    }

}
