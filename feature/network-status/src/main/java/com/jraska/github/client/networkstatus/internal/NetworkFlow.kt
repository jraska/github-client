package com.jraska.github.client.networkstatus.internal

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

internal class NetworkFlow @Inject constructor(
  private val context: Context
) {
  private val networkFlow: Flow<Boolean> by lazy {
    setupNetworkFlow()
  }

  fun connectedFlow(): Flow<Boolean> {
    return networkFlow.distinctUntilChanged()
  }

  private val connectivityManager
    get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  private fun setupNetworkFlow(): Flow<Boolean> {
    val sharedFlow = MutableSharedFlow<Boolean>(replay = 1, extraBufferCapacity = 1, BufferOverflow.DROP_OLDEST)

    connectivityManager.registerNetworkCallback(allChangesRequest(), object : ConnectivityManager.NetworkCallback() {
      override fun onAvailable(network: Network) {
        sharedFlow.tryEmit(isConnected())
      }

      override fun onLost(network: Network) {
        sharedFlow.tryEmit(isConnected())
      }
    })

    return sharedFlow
  }

  private fun allChangesRequest() = NetworkRequest.Builder().build()

  private fun isConnected(): Boolean {
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
  }
}
