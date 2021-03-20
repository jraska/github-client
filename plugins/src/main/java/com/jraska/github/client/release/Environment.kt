package com.jraska.github.client.release

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.lang.IllegalStateException

class Environment(
  val apiToken: String,
  val baseUrl: HttpUrl = "https://api.github.com/repos/jraska/github-client/".toHttpUrl()
) {
  companion object {
    fun create(): Environment {
      val apiToken = System.getenv("TOKEN_GITHUB_API") ?: throw IllegalStateException("GitHub API token missing")
      return Environment(apiToken)
    }
  }
}
