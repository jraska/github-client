package com.jraska.github.client.core.android

import android.app.Activity
import android.app.Application
import android.os.Bundle
import javax.inject.Inject

import javax.inject.Provider

class TopActivityProvider : Provider<Activity> {
  private var topActivity: Activity? = null

  private val callbacks: Application.ActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
      topActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {
      topActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

    override fun onActivityDestroyed(activity: Activity) {}
  }

  override fun get(): Activity {
    if (topActivity == null) {
      throw IllegalStateException("No activity")
    }

    return topActivity!!
  }

  class OnCreateSetup @Inject constructor(private val topActivityProvider: TopActivityProvider) : OnAppCreate {
    override fun onCreate(app: Application) {
      app.registerActivityLifecycleCallbacks(topActivityProvider.callbacks)
    }
  }
}
