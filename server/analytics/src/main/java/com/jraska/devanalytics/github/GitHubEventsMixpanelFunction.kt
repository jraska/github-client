package com.jraska.devanalytics.github

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import com.jraska.devanalytics.github.model.Environment
import com.jraska.devanalytics.github.report.GitHubEventMixpanelInterceptor

class GitHubEventsMixpanelFunction : HttpFunction {
  override fun service(gitHubWebHook: HttpRequest, response: HttpResponse) {
    val interceptor = GitHubEventMixpanelInterceptor.create(Environment.create())

    val event = interceptor.intercept(gitHubWebHook.reader)

    response.writer.write("Event $event reported to Mixpanel")
    response.setStatusCode(200)
  }
}
