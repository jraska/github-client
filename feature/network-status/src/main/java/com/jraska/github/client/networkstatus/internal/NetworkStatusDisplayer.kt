package com.jraska.github.client.networkstatus.internal

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.jraska.github.client.core.android.DefaultActivityCallbacks
import com.jraska.github.client.coroutines.AppDispatchers
import com.jraska.github.client.networkstatus.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import timber.log.Timber
import javax.inject.Inject

internal class NetworkStatusDisplayer @Inject constructor(
  private val networkChannel: NetworkChannel,
  private val appDispatchers: AppDispatchers
) {

  private val job: Job? = null
  private var offlineSnackbar: Snackbar? = null

  fun onActivityResumed(activity: Activity) {
    if (activity is AppCompatActivity) {

    }
//    networkChannel.connectedFlow()
//      .collect(showState())
//      .observeOn(appSchedulers.mainThread)
//      .subscribe { showState(activity, it) }
//      .also { compositeDisposable.add(it) }
  }

  fun onActivityPaused() {
    dismissAnySnackbar()
    job?.cancel()
  }

  private fun dismissAnySnackbar() {
    offlineSnackbar?.dismiss()
    offlineSnackbar = null
  }

  private fun showState(activity: Activity, isOnline: Boolean) {
    if (isOnline) {
      dismissAnySnackbar()
    } else {
      showOfflineSnackbar(activity)
    }
  }

  private fun showOfflineSnackbar(activity: Activity) {
    Timber.i("Showing offline snackbar in %s", activity::class.java.name)

    dismissAnySnackbar()

    val contentView = activity.findViewById<View>(android.R.id.content)
    val viewForSnackbar = findCoordinatorLayout(contentView) ?: contentView

    val snackbar = Snackbar.make(viewForSnackbar, R.string.status_is_offline, Snackbar.LENGTH_INDEFINITE)
    snackbar.show()
    offlineSnackbar = snackbar
  }

  private fun findCoordinatorLayout(view: View): CoordinatorLayout? {
    if (view is CoordinatorLayout) {
      return view
    }

    if (view is ViewGroup) {
      val childCount = view.childCount
      for (childViewIndex in 0 until childCount) {
        val childView = view.getChildAt(childViewIndex)

        val coordinatorLayout = findCoordinatorLayout(childView)
        if (coordinatorLayout != null) {
          return coordinatorLayout
        }
      }
    }

    return null
  }

  internal class Callbacks(
    private val displayer: NetworkStatusDisplayer
  ) : DefaultActivityCallbacks() {

    override fun onActivityResumed(activity: Activity) {
      displayer.onActivityResumed(activity)
    }

    override fun onActivityPaused(activity: Activity) {
      displayer.onActivityPaused()
    }
  }
}
