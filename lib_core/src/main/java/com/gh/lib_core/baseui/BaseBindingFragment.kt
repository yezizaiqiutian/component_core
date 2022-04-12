package com.gh.lib_core.baseui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
abstract class BaseBindingFragment<D : ViewDataBinding, V : ViewModel> : BaseFragment() {

    protected lateinit var mContext: Context

    protected lateinit var binding: D

    protected lateinit var viewModel: V


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        getViewModelClass()?.let { viewModel = ViewModelProvider(this).get(it) }
        initObserver()
        onInit()
        return binding.root
    }

    abstract fun getLayoutId(): Int

    open fun onViewClick(v: View) {}

    open fun onInit() {}

    open fun initObserver() {}

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