package com.gh.lib_core.baseui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author: gh
 * @description:
 * @date: 2020/9/22.
 * @from:
 */
public class BaseFragment extends Fragment {

    protected Activity activity;
    protected Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        context = getActivity();

    }

}
