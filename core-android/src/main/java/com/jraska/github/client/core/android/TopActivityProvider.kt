package com.jraska.github.client.core.android

import android.app.Activity
import android.app.Application
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class TopActivityProvider {
  private val topActivitySubject = BehaviorSubject.createDefault<Any>(NO_ACTIVITY)
  private val resumedActivitySubject = BehaviorSubject.createDefault<Any>(NO_ACTIVITY)

  fun topActivity(): Single<Activity> {
    return topActivitySubject.filter { it is Activity }
      .take(1)
      .cast(Activity::class.java)
      .firstOrError()
  }

  fun resumedActivity(): Single<Activity> {
    return resumedActivitySubject.filter { it is Activity }
      .take(1)
      .cast(Activity::class.java)
      .firstOrError()
  }

  fun topActivitySync(): Activity? {
    val value = topActivitySubject.value
    if (value is Activity) {
      return value
    } else {
      return null
    }
  }

  private val callbacks: Application.ActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
      topActivitySubject.onNext(activity)
    }

    override fun onActivityStarted(activity: Activity) {
      topActivitySubject.onNext(activity)
    }

    override fun onActivityResumed(activity: Activity) {
      topActivitySubject.onNext(activity)
      resumedActivitySubject.onNext(activity)
    }

    override fun onActivityPaused(activity: Activity) {
      if (resumedActivitySubject.value == activity) {
        resumedActivitySubject.onNext(NO_ACTIVITY)
      }

      topActivitySubject.onNext(NO_ACTIVITY)
    }

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

    override fun onActivityDestroyed(activity: Activity) {}
  }

  class RegisterCallbacks @Inject constructor(private val topActivityProvider: TopActivityProvider) : OnAppCreate {
    override fun onCreate(app: Application) {
      app.registerActivityLifecycleCallbacks(topActivityProvider.callbacks)
    }
  }

  companion object {
    val NO_ACTIVITY = Unit
  }
}
