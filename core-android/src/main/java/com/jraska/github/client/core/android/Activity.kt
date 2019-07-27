package com.jraska.github.client.core.android

import android.app.Activity
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun Activity.inputUrl(): HttpUrl {
  return intent.data.toString().toHttpUrl()
}
