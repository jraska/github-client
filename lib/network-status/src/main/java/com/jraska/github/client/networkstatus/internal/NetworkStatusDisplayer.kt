package com.jraska.github.client.networkstatus.internal

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.jraska.github.client.core.android.DefaultActivityCallbacks
import com.jraska.github.client.networkstatus.R
import com.jraska.github.client.rx.AppSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

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
    dismissAnySnackbar()

    val view = activity.findViewById<View>(android.R.id.content)

    val snackbar = Snackbar.make(view, R.string.status_is_offline, Snackbar.LENGTH_INDEFINITE)
    snackbar.show()
    offlineSnackbar = snackbar
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



