package com.gh.lib_core.baseui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.gh.lib_core.utils.ActivityManager
import com.wawj.core.baseui.BaseViewModel
import java.lang.reflect.ParameterizedType


/**
 * @Description: BaseBindingActivity.java
 * @Author piyouqing
 * @Date 2020/08/14 10:40
 * @Version V1.0
 *
 * 如果Activity没有ViewModel，泛型V传BaseViewModel
 */
abstract class BaseBindingActivity<D : ViewDataBinding, V : ViewModel> : BaseActivity() {
    protected lateinit var binding: D
    protected lateinit var viewModel: V
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.getInstance().addActivity(this)
        ARouter.getInstance().inject(this)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 取消标题

        binding = DataBindingUtil.setContentView(this, getLayoutId()) as D
        binding.lifecycleOwner = this

        getViewModelClass()?.let {
            viewModel = ViewModelProvider(this).get(it)
        }

        val intent = intent
        intent?.let {
            getIntentData(it)
        }

        initObserver()
        onInit()
    }

    open fun getIntentData(intent: Intent) {

    }

    abstract fun getLayoutId(): Int

    open fun initObserver() {}
    open fun onInit() {}
    open fun onViewClick(v: View) {}

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.getInstance().finishActivity(this)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewModelClass(): Class<V>? {
        return try {
            val cls: Class<V>? =
                (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as? Class<V>
            if (cls == BaseViewModel::class.java) null else cls
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}