package com.jraska.github.client.android.test.http

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.MockResponse

fun assetJson(path: String): MockResponse {
  val assetsStream = InstrumentationRegistry.getInstrumentation().context.assets.open(path)

  val json = assetsStream.bufferedReader().readText()
  return MockResponse().setResponseCode(200).setBody(json)
}
