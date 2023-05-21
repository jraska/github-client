package com.jraska.github.client.http

import okhttp3.HttpUrl
class NetworkResource(
  val url: HttpUrl,
  val statusCode: Int,
  val method: String,
  val durationMs: Long,
  val requestId: String,
  val responseContentLength: Long,
  val responseContentType: String,
  val message: String,
  val requestContentLength: Long,
  val requestContentType: String,
)
