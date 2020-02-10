package com.jraska.github.client.networkstatus.internal

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.jraska.github.client.core.android.DefaultActivityCallbacks
import com.jraska.github.client.networkstatus.R
import com.jraska.github.client.rx.AppSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import timber.log.Timber

internal class NetworkStatusDisplayer @Inject constructor(
  private val networkObservable: NetworkObservable,
  private val appSchedulers: AppSchedulers
) {

  private val compositeDisposable = CompositeDisposable()
  private var offlineSnackbar: Snackbar? = null

  fun onActivityResumed(activity: Activity) {
    networkObservable.connectedObservable()
      .observeOn(appSchedulers.mainThread)
      .subscribe { showState(activity, it) }
      .also { compositeDisposable.add(it) }
  }

  fun onActivityPaused() {
    dismissAnySnackbar()
    compositeDisposable.clear()
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
