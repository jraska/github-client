package com.jraska.github.client.core.android

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Looper
import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import okhttp3.internal.toImmutableList
import javax.inject.Inject

class TopActivityProviderImpl(
  private val isMainThread: () -> Boolean = { Looper.getMainLooper() == Looper.myLooper() }
) : TopActivityProvider {

  private val pendingActions = mutableListOf<(Activity) -> Unit>()
  override var topActivity: Activity? = null
    private set(activity) {
      field = activity
      executePendingActions()
    }

  @AnyThread
  override fun onTopActivity(action: (Activity) -> Unit) {
    val topActivity = topActivity

    if (topActivity == null) {
      addPendingAction(action)
    } else {
      if (isMainThread()) {
        action(topActivity)
      } else {
        addPendingAction(action)
        topActivity.runOnUiThread { executePendingActions() }
      }
    }
  }

  @MainThread
  private fun executePendingActions() {
    val activity = topActivity ?: return

    val actionsToExecute: List<(Activity) -> Unit>
    synchronized(pendingActions) {
      actionsToExecute = pendingActions.toImmutableList()
      pendingActions.clear()
    }

    actionsToExecute.forEach { it(activity) }
  }

  @AnyThread
  private fun addPendingAction(action: (Activity) -> Unit) {
    synchronized(pendingActions) {
      pendingActions.add(action)
    }
  }

  internal val callbacks: Application.ActivityLifecycleCallbacks = object : DefaultActivityCallbacks() {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
      topActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
      topActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
      topActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
      topActivity = null
    }
  }

  private fun setupWith(app: Application) {
    app.registerActivityLifecycleCallbacks(callbacks)
  }

  class RegisterCallbacks @Inject constructor(private val topActivityProvider: TopActivityProviderImpl) : OnAppCreate {
    override fun onCreate(app: Application) {
      topActivityProvider.setupWith(app)
    }
  }
}
