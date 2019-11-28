package com.jraska.github.client

import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.logging.CrashReporter
import com.jraska.github.client.rx.AppSchedulers
import io.reactivex.schedulers.Schedulers

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

  fun testTimeProvider(elapsed: Long = 0): TestTimeProvider {
    return TestTimeProvider(elapsed)
  }

  fun trampoline(): AppSchedulers {
    return AppSchedulers(Schedulers.trampoline(), Schedulers.trampoline(), Schedulers.trampoline())
  }

  fun emptyCrashReporter(): CrashReporter {
    return EmptyCrashReporter
  }

  fun recordingAnalyticsProperty(): RecordingAnalyticsProperty {
    return RecordingAnalyticsProperty()
  }
}
