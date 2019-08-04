package com.jraska.github.client.core.android

import android.app.Activity
import android.net.Uri
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun Activity.inputUrl(): HttpUrl {
  val inputUri = intent.data ?: throw IllegalArgumentException("No uri provided")

  return replaceNoHttpScheme(inputUri)
    .toString()
    .toHttpUrl()
}

private fun replaceNoHttpScheme(uri: Uri): Uri {
  if (uri.scheme == "http" || uri.scheme == "https") {
    return uri
  }

  return uri.buildUpon()
    .scheme("https")
    .build()
}
