package com.jraska.github.client

import android.app.Activity
import okhttp3.HttpUrl

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun Activity.inputUrl() : HttpUrl {
  return HttpUrl.get(intent.data.toString())
}
