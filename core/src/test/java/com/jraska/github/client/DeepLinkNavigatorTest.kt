package com.jraska.github.client

import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class DeepLinkNavigatorTest {
  @Test
  fun whenStartUserDetail_thenCorrectDeepLinkLaunched() {
    val deepLinkLauncher = mock(DeepLinkLauncher::class.java)
    val webLinkLauncher = mock(WebLinkLauncher::class.java)
    val navigator = DeepLinkNavigator(deepLinkLauncher, webLinkLauncher)

    navigator.startUserDetail("johny")

    verify(deepLinkLauncher).launch("https://github.com/johny".toHttpUrl())
  }
}
