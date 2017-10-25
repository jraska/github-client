package com.jraska.github.client

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.jraska.github.client.ui.BaseActivity

import javax.inject.Provider

class TopActivityProvider internal constructor() : Provider<BaseActivity> {
  private var topActivity: BaseActivity? = null

  val callbacks: Application.ActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
      topActivity = activity as BaseActivity
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {
      topActivity = activity as BaseActivity
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

    override fun onActivityDestroyed(activity: Activity) {}
  }

  override fun get(): BaseActivity {
    if (topActivity == null) {
      throw IllegalStateException("No activity")
    }

    return topActivity!!
  }
}
