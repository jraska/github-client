package com.jraska.github.client.common

import com.jraska.github.client.analytics.toAnalyticsString
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class HttpUrlTest {
  @Test
  fun toAnalyticsStringRemovesParameters() {
    val url = "https://github.com/jraska/Falcon?param=johny&pii=johny@domain.com&id=xxx".toHttpUrl()

    val toAnalyticsString = url.toAnalyticsString()

    assertThat(toAnalyticsString).isEqualTo("https://github.com/jraska/Falcon")
  }
}
