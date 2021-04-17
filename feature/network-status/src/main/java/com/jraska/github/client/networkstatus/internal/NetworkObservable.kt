package com.jraska.github.client.networkstatus.internal

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

internal class NetworkObservable @Inject constructor(
  private val context: Context
) {
  private val networkObservable: Observable<Boolean> by lazy {
    setupNetworkObservable()
  }

  fun connectedObservable(): Observable<Boolean> {
    return networkObservable
  }

  private val connectivityManager
    get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  private fun setupNetworkObservable(): Observable<Boolean> {
    val behaviorSubject = BehaviorSubject.createDefault(isConnected())

    connectivityManager.registerNetworkCallback(allChangesRequest(), object : ConnectivityManager.NetworkCallback() {
      override fun onAvailable(network: Network) {
        behaviorSubject.onNext(isConnected())
      }

      override fun onLost(network: Network) {
        behaviorSubject.onNext(isConnected())
      }
    })

    return behaviorSubject.distinctUntilChanged()
  }

  private fun allChangesRequest() = NetworkRequest.Builder().build()

  private fun isConnected(): Boolean {
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
  }
}
