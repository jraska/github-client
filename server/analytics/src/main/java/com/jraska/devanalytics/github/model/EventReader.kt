package com.jraska.devanalytics.github.model

import com.google.gson.Gson
import java.io.BufferedReader

class EventReader(
  private val gson: Gson
) {
  fun parse(reader: BufferedReader): GitHubPrEvent {
    val dto = gson.getAdapter(GitHubEventDto::class.java).fromJson(reader)

    val action = dto.action
    when {
      action == "assigned" || action == "unassigned" -> return assignedEvent(dto)
      action == "labeled" || action == "unlabeled" -> return labeledEvent(dto)
      action == "synchronize" -> return synchronizeEvent(dto)

      dto.requestedReviewer != null -> {
        return requestedReviewEvent(dto)
      }
      dto.review != null -> {
        return reviewCreatedEvent(dto)
      }
      dto.comment != null -> {
        return commentEvent(dto)
      }
      dto.pullRequest != null -> {
        return createPrEvent(dto)
      }
      else -> throw IllegalArgumentException("Unknown event $dto")
    }

  }

  private fun labeledEvent(dto: GitHubEventDto): GitHubPrEvent {
    return GitHubPrEvent(
      if (dto.action == "unlabeled") EventNames.PR_UNLABELED else EventNames.PR_LABELED,
      dto.action,
      dto.sender.login,
      dto.pullRequest!!.prUrl,
      dto.pullRequest!!.number,
      dto.pullRequest!!.body,
      dto.pullRequest!!.state
    )
  }

  private fun synchronizeEvent(dto: GitHubEventDto): GitHubPrEvent {
    return GitHubPrEvent(
      EventNames.PR_SYNC,
      dto.action,
      dto.sender.login,
      dto.pullRequest!!.prUrl,
      dto.pullRequest!!.number,
      dto.pullRequest!!.body,
      dto.pullRequest!!.state
    )
  }

  private fun assignedEvent(dto: GitHubEventDto): GitHubPrEvent {
    return GitHubPrEvent(
      if (dto.action == "unassigned") EventNames.PR_UNASSIGNED else EventNames.PR_ASSIGNED,
      dto.action,
      dto.sender.login,
      dto.pullRequest!!.prUrl,
      dto.pullRequest!!.number,
      dto.pullRequest!!.body,
      dto.pullRequest!!.state
    )
  }

  private fun createPrEvent(dto: GitHubEventDto): GitHubPrEvent {
    val eventName = if (dto.action == "opened") {
      EventNames.PR_OPEN
    } else if (dto.action == "closed") {
      if (dto.pullRequest!!.merged) EventNames.PR_MERGE else EventNames.PR_CLOSE
    } else if (dto.action == "edited") {
      EventNames.PR_EDIT
    } else {
      throw IllegalArgumentException("Unknown event $dto")
    }

    return GitHubPrEvent(
      eventName,
      dto.action,
      dto.sender.login,
      dto.pullRequest!!.prUrl,
      dto.pullRequest!!.number,
      dto.pullRequest!!.body,
      dto.pullRequest!!.state
    )
  }

  private fun commentEvent(dto: GitHubEventDto): GitHubPrEvent {
    return GitHubPrEvent(
      if (dto.action == "deleted") EventNames.PR_COMMENT_DELETED else if (dto.action == "edited") EventNames.PR_COMMENT_EDIT else EventNames.PR_COMMENT,
      dto.action,
      dto.sender.login,
      dto.issue!!.pullRequest!!.prUrl,
      dto.issue!!.number,
      dto.comment!!.body,
      dto.issue!!.state
    )
  }

  private fun reviewCreatedEvent(dto: GitHubEventDto): GitHubPrEvent {
    return GitHubPrEvent(
      EventNames.REVIEW_CREATED,
      dto.action,
      dto.review!!.user.login,
      dto.pullRequest!!.prUrl,
      dto.pullRequest!!.number,
      dto.review!!.body,
      dto.review?.state
    )
  }

  private fun requestedReviewEvent(dto: GitHubEventDto): GitHubPrEvent {
    return GitHubPrEvent(
      EventNames.REVIEW_REQUEST,
      dto.action,
      dto.sender.login,
      dto.pullRequest!!.prUrl,
      dto.pullRequest!!.number,
      null,
      dto.pullRequest!!.state
    )
  }

  companion object {
    fun create(): EventReader {
      return EventReader(Gson())
    }
  }
}
