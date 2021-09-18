package com.jraska.github.client.networkstatus.internal

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class NetworkChannel @Inject constructor(
  private val context: Context
) {
  private val networkFlow: Flow<Boolean> by lazy {
    setupNetworkFlow()
  }

  fun connectedFlow(): Flow<Boolean> {
    return networkFlow.onStart { emit(isConnected()) }
      .distinctUntilChanged()
  }

  private val connectivityManager
    get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  private fun setupNetworkFlow(): Flow<Boolean> {
    val publishSubject = MutableSharedFlow<Boolean>()

    connectivityManager.registerNetworkCallback(allChangesRequest(), object : ConnectivityManager.NetworkCallback() {
      override fun onAvailable(network: Network) {
        publishSubject.tryEmit(isConnected())
      }

      override fun onLost(network: Network) {
        publishSubject.tryEmit(isConnected())
      }
    })

    return publishSubject
  }

  private fun allChangesRequest() = NetworkRequest.Builder().build()

  private fun isConnected(): Boolean {
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
  }
}
