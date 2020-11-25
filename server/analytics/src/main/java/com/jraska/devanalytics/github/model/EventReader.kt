package com.jraska.devanalytics.github.model

import com.google.gson.Gson
import java.io.BufferedReader

class EventReader(
  private val gson: Gson
) {
  fun parse(reader: BufferedReader): GitHubPrEvent {
    val dto = gson.getAdapter(GitHubEventDto::class.java).fromJson(reader)

    val action = dto.action
    var comment = dto.comment?.body
    if (comment == null && action == "opened") {
      comment = dto.pullRequest?.body
    }

    if (comment == null && action == "created") {
      comment = dto.review?.body
    }

    val login = dto.review?.user?.login ?: dto.sender.login

    return GitHubPrEvent(
      action,
      login,
      dto.pullRequest?.prUrl ?: dto.issue?.pullRequest?.prUrl ?: throw UnsupportedOperationException("PR url not found"),
      dto.pullRequest?.number ?: dto.issue?.number ?: throw IllegalStateException("PR number not found"),
      comment,
      dto.review?.state
    )
  }

  companion object {
    fun create(): EventReader {
      return EventReader(Gson())
    }
  }
}
