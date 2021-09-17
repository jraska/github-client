package com.jraska.github.client

import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.android.RecordingDeepLinkLauncher
import com.jraska.github.client.coroutines.AppDispatchers
import kotlinx.coroutines.Dispatchers

object Fakes {
  fun config(values: Map<String, Any> = emptyMap()): FakeConfig {
    return FakeConfig.create(values)
  }

  fun emptyAnalytics(): EventAnalytics {
    return EmptyAnalytics()
  }

  fun recordingAnalytics(): RecordingEventAnalytics {
    return RecordingEventAnalytics()
  }

  fun unconfined(): AppDispatchers {
    return AppDispatchers(Dispatchers.Unconfined, Dispatchers.Unconfined)
  }

  fun recordingAnalyticsProperty(): RecordingAnalyticsProperty {
    return RecordingAnalyticsProperty()
  }

  fun recordingDeepLinkLauncher(): RecordingDeepLinkLauncher {
    return RecordingDeepLinkLauncher()
  }
}
