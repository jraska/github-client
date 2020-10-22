package com.jraska.github.client.inappupdate

import android.annotation.SuppressLint
import com.jraska.github.client.rx.AppSchedulers
import io.reactivex.Completable
import io.reactivex.internal.functions.Functions
import timber.log.Timber
import javax.inject.Inject

internal class UpdateCheckScheduler @Inject constructor(
  private val updateChecker: UpdateChecker,
  private val appSchedulers: AppSchedulers
) {

  @SuppressLint("CheckResult") // Fire and forget
  fun startNonBlockingCheck() {
    Timber.d("Starts")
    Completable.fromAction { updateChecker.checkForUpdates() }
      .subscribeOn(appSchedulers.io)
      .subscribe(Functions.EMPTY_ACTION, { Timber.e(it, "Scheduled version check failed") })
  }
}
