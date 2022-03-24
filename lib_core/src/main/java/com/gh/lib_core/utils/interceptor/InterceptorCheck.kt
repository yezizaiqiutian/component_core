package com.gh.lib_core.utils.interceptor

import android.app.Activity
import android.content.Context
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author: gh
 * @description:
 * @date: 2022/3/23.
 * @from: 官网
 */
/**
 * 这是一个条件拦截的工具
 * 适用于为目的添加多个前置条件，当所有前置条件都满足后，才执行目的。
 * 比如：只有登录后，才能跳转个人账户页面。
 *       此工具可做到，用户没有登录时，先跳转登录页面，当用户登录后自动跳转个人账户页面。
 *
 * 满足条件后需要调continueCheck()，
 * 但注意比如有一种情况需要区分：用户主动点击对的登录按钮进入的登录页面，登录后不能调用continueCheck()。
 * 等待满足条件时，如果终止了，需要调用abortCheck(),以免发生bug。
 */
class InterceptorCheck {

    //目标代码
    private var target: Target? = null

    //条件队列,先添加的先检测
    private var conditionsQueue: ArrayDeque<Interceptor> = ArrayDeque()

    //记录上一个条件,没有被满足的条件
    private var lastInterceptor: Interceptor? = null

    //状态回调监听
    private var checkState: CheckStateListener? = null

    companion object {
        val instance = Holder.holder
    }

    //kotlin单例实现方式(第五种)
    //https://blog.csdn.net/qq_23025319/article/details/107061895
    private object Holder {
        val holder = InterceptorCheck()
    }

    //设置目标代码,所有条件都满足后自动调用此Target的onAllConditionsMet函数
    fun setTarget(target: Target): InterceptorCheck {
        clear()
        this.target = target
        return this
    }

    //添加条件
    fun addInterceptor(vararg interceptor: Interceptor?): InterceptorCheck {
        interceptor.forEach {
            it?.let { conditionsQueue.add(it) }
        }
        return this
    }

    //开始检查
    fun startCheck() {

        //检查上一个条件的结果
        lastInterceptor?.let {
            if (it.checkCondition()) {
                checkState?.onOneConditionError(it)
            } else {
                //如果上一个条件没有通过,是不允许再发起call的
                return
            }
        }


        if (conditionsQueue.size == 0) {
            //通过所有条件后,执行目标代码
            target?.onAllConditionnsSuccess()
            clear()
        } else {
            //还有未检查的条件
            val interceptor = conditionsQueue.poll()
            interceptor?.let {
                if (it.checkCondition()) {
                    //条件检查通过
                    checkState?.onOneConditionError(it)
                    //满足条件,触发下一个条件检查
                    startCheck()
                } else {
                    //条件检查不通过
                    lastInterceptor = it
                    checkState?.onOneConditionSuccess(it)
                    it.toGetCondition()
                }
            }
        }

    }

    fun setCheckStateListener(checkStateListener: CheckStateListener): InterceptorCheck {
        this.checkState = checkState
        return this
    }

    fun continueCheck() {
        startCheck()
    }

    fun abortCheck() {
        checkState?.onCheckAbort()
        clear()
    }

    private fun clear() {
        conditionsQueue.clear()
        target = null
        lastInterceptor = null
        checkState = null
    }

}


/**
 * 目标行为
 */
//为什么使用fun interface?方便使用者lambda表达式
//https://www.cnblogs.com/sowhappy/p/15153944.html
fun interface Target {

    //条件全部通过后执行
    fun onAllConditionnsSuccess()

}

/**
 * 条件拦截器
 */
abstract class Interceptor {

    //使用弱引用,防止单例引起的内存泄漏
    @JvmField
    var weakContext: WeakReference<Context>? = null

    @JvmField
    var weakActivity: WeakReference<Activity>? = null

    //拦截器名称
    open fun getName(): String = ""

    //检查条件是否满足
    abstract fun checkCondition(): Boolean

    //如果不满足条件,调用此函数
    abstract fun toGetCondition()

}

/**
 * 状态回调监听
 */
interface CheckStateListener {

    //每当一个条件不满足时回调
    fun onOneConditionSuccess(interceptor: Interceptor)

    //每当一个条件满足后回调
    fun onOneConditionError(interceptor: Interceptor)

    //条件检查流程中断(调用abortCheck后触发)
    fun onCheckAbort()

}