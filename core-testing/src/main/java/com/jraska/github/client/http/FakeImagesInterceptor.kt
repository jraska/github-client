package com.jraska.github.client.http

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class FakeImagesInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()

    if (isImage(request)) {
      return fakeImageResponse(request)
    } else {
      return chain.proceed(request)
    }
  }

  private fun isImage(request: Request): Boolean {
    return request.url.host.contains("githubusercontent.com")
  }

  private fun fakeImageResponse(request: Request): Response {
    return Response.Builder()
      .request(request)
      .header("content-type", "image/png")
      .protocol(Protocol.HTTP_1_1)
      .code(200)
      .message("OK fake image")
      .body(AVATAR_PNG.toResponseBody("image/png".toMediaType()))
      .sentRequestAtMillis(-1L)
      .receivedResponseAtMillis(System.currentTimeMillis())
      .build()
  }
}

private val AVATAR_PNG = arrayOf<Byte>(
  -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68,
  82, 0, 0, 0, 8, 0, 0, 0, 8, 8, 6, 0, 0, 0, -60, 15, -66, -117, 0, 0, 0, 6, 98, 75, 71, 68, 0, 0,
  0, 0, 0, 0, -7, 67, -69, 127, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 11, 19, 0, 0, 11, 19, 1, 0,
  -102, -100, 24, 0, 0, 0, 7, 116, 73, 77, 69, 7, -25, 5, 7, 21, 47, 10, 122, -122, -122, -69, 0,
  0, 0, -56, 73, 68, 65, 84, 24, -45, 77, -118, -67, 78, 2, 65, 24, 0, -25, 91, -31, -72, 35, 43,
  33, -124, 24, 27, 53, 36, -38, 90, -7, 76, 62, -111, 86, 62, -124, -15, 69, 32, -76, 104, 66,
  -127, 114, 2, 7, -25, -49, -103, -107, 61, 118, 63, 11, 49, 113, -70, -103, -116, -52, -117,
  -75, 22, -50, 115, 98, 51, 68, 0, 64, -127, -39, -89, -93, -97, 38, -104, -62, 121, -18, -97,
  75, 124, -116, 4, 85, -126, 42, 117, -120, 60, -68, -108, 44, -99, 71, -54, -14, 77, 125, -116,
  -36, 61, -66, -30, 85, 1, 104, -118, 112, 125, 113, 76, 98, 12, -14, -108, 47, 117, -68, -87,
  24, -40, -108, -31, -90, 34, 2, 87, 61, -53, -76, -6, -26, -78, 103, 49, 95, -69, -64, -24, -35,
  113, 122, -104, -47, 77, 26, 116, -101, 7, -100, 117, 50, -58, 31, -114, -86, -34, 97, -40, 19,
  84, 17, 64, -128, 16, -11, 47, -1, 14, -75, 42, -73, -109, -100, -127, 77, 57, -17, -76, -71,
  -103, -28, 108, -9, -109, -52, 87, 107, 93, -72, 45, 0, 71, 89, 11, 1, -2, -5, 15, -104, -108,
  98, -121, 85, -25, -6, -42, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126
).toByteArray()
