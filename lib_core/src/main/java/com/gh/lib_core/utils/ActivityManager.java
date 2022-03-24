package com.gh.lib_core.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * @author: gh
 * @description: Activity管理类：用于Activity管理和应用程序退出
 * @date: 2022/3/23.
 * @from: 官网
 */
final public class ActivityManager {

    private static Stack<Activity> activityManager;

    private static ActivityManager instance;

    private static ActivityManager getInstance() {
        if (null == instance) {
            instance = new ActivityManager();
            activityManager = new Stack<>();
        }
        return instance;
    }

    /**
     * 获取当前Activity栈中元素个数
     */
    public int getCount() {
        return activityManager.size();
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        activityManager.add(activity);
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public Activity findTopActivity() {
        if (activityManager == null || activityManager.isEmpty()) {
            return null;
        }
        return activityManager.lastElement();
    }

    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public Activity findActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity a : activityManager) {
            if (a.getClass().equals(cls)) {
                activity = a;
                break;
            }
        }
        return activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public synchronized void finishTopActivity() {
        finishActivity(findTopActivity());
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public synchronized void finishActivity(Activity activity) {
        if (activity != null) {
            activityManager.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定的Activity(
     *
     * @param cls
     */
    public synchronized void finishActivity(Class<?> cls) {
        for (Activity activity : activityManager) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        for (Activity activity : activityManager) {
            if (!(activity.getClass().equals(cls))) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityManager) {
            finishActivity(activity);
        }
        activityManager.clear();
    }

    /**
     * 应用程序退出
     */
    public void appExit() {
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(-1);
        }
    }

}
