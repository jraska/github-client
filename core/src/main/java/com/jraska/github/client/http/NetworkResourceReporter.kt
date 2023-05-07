package com.jraska.github.client.http

import com.jraska.github.client.analytics.EventAnalytics
import timber.log.Timber
import javax.inject.Inject

class NetworkResourceReporter @Inject constructor(
  private val eventAnalytics: EventAnalytics
) {
  fun report(resource: NetworkResource) {
    Timber.d(resource.toString())
  }
}
