package com.gh.lib_core.baseui

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.tencent.mmkv.MMKV

/**
 * @author: gh
 * @description:
 * @date: 2022/4/14.
 * @from:
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MMKV.initialize(this)
        ARouter.init(this)

    }


}