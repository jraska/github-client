package com.jraska.github.client.http

import okhttp3.HttpUrl

data class NetworkResource(
  val url: HttpUrl,
  val responseCode: Int,
  val method: String,
  val durationMs: Long,
  val requestId: String,
  val responseContentLength: Long,
  val message: String,
  val requestContentLength: Long
)
