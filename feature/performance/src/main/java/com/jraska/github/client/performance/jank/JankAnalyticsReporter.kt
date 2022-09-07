package com.jraska.github.client.performance.jank

import androidx.metrics.performance.FrameData
import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.NANOSECONDS
import javax.inject.Inject

private val TOO_LARGE_JANK_NANOS = NANOSECONDS.convert(3 * 16, MILLISECONDS)
private val JANK_ANALYTICS_NAME = AnalyticsEvent.Key("performance_ui_jank", Owner.PERFORMANCE_TEAM)
private const val JANK_TIME_PROPERTY = "value"

class JankAnalyticsReporter @Inject constructor(
  private val eventAnalytics: EventAnalytics
) {
  fun reportJank(frame: FrameData) {
    Timber.v("Jank of %s", frame)

    if (frame.frameDurationUiNanos >= TOO_LARGE_JANK_NANOS) {
      reportJankToAnalytics(frame)
    }
  }

  private fun reportJankToAnalytics(frame: FrameData) {
    val jankMs = NANOSECONDS.toMillis(frame.frameDurationUiNanos)

    val event = AnalyticsEvent.builder(JANK_ANALYTICS_NAME)
      .addProperty(JANK_TIME_PROPERTY, jankMs)
      .build()

    eventAnalytics.report(event)
    Timber.d("Jank of %s ms reported to analytics", jankMs)
  }
}
