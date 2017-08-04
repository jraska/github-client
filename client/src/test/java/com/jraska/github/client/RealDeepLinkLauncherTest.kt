package com.jraska.github.client

import android.content.Intent
import com.jraska.github.client.ui.BaseActivity
import okhttp3.HttpUrl
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class RealDeepLinkLauncherTest {
  @Mock private lateinit var activity: BaseActivity
  private lateinit var deepLinkLauncher: RealDeepLinkLauncher

  @Before
  fun before() {
    MockitoAnnotations.initMocks(this)

    deepLinkLauncher = RealDeepLinkLauncher { activity }
  }

  @Test(expected = IllegalArgumentException::class)
  fun whenUnknownHostUrl_thenException() {
    deepLinkLauncher.launch(HttpUrl.parse("https://jraska.com/users"))
  }

  @Test(expected = IllegalArgumentException::class)
  fun whenUnsupportedUrl_thenIllegalStateException() {
    deepLinkLauncher.launch(HttpUrl.parse("https://github.com/jraska/repos/johny"))
  }

  @Test
  fun whenUsersUrl_thenPasses() {
    deepLinkLauncher.launch(HttpUrl.parse("https://github.com/users"))

    verify<BaseActivity>(activity).startActivity(any<Intent>())
  }

  @Test
  fun whenRepoUrl_thenPasses() {
    deepLinkLauncher.launch(HttpUrl.parse("https://github.com/jraska/Falcon"))

    verify<BaseActivity>(activity).startActivity(any<Intent>())
  }

  @Test
  fun whenUserDetailUrl_thenUsersActivityStarts() {
    deepLinkLauncher.launch(HttpUrl.parse("https://github.com/jraska"))

    verify<BaseActivity>(activity).startActivity(any<Intent>())
  }
}
