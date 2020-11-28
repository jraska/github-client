package com.jraska.devanalytics.github.model

import com.google.gson.annotations.SerializedName

class GitHubEventDto {
  @SerializedName("action")
  lateinit var action: String

  @SerializedName("sender")
  lateinit var sender: UserDto

  @SerializedName("pull_request")
  var pullRequest: PullRequestDto? = null

  @SerializedName("issue")
  var issue: IssueDto? = null

  @SerializedName("comment")
  var comment: CommentDto? = null

  @SerializedName("review")
  var review: ReviewDto? = null

  @SerializedName("requested_reviewer")
  var requestedReviewer: UserDto? = null
}

class UserDto {
  @SerializedName("login")
  lateinit var login: String
}

class PullRequestDto {
  @SerializedName("html_url")
  lateinit var prUrl: String

  @SerializedName("number")
  var number: Int = 0

  @SerializedName("state")
  var state: String? = null

  @SerializedName("body")
  var body: String = ""

  @SerializedName("merged")
  var merged: Boolean = false

  @SerializedName("merged_by")
  var mergedBy: UserDto? = null
}

class IssuePullRequestDto {
  @SerializedName("html_url")
  lateinit var prUrl: String
}

class IssueDto {
  @SerializedName("pull_request")
  var pullRequest: IssuePullRequestDto? = null

  @SerializedName("number")
  var number: Int = 0

  @SerializedName("state")
  var state: String? = null
}

class CommentDto {
  @SerializedName("body")
  lateinit var body: String
}

class ReviewDto {
  @SerializedName("user")
  lateinit var user: UserDto

  @SerializedName("state")
  lateinit var state: String

  @SerializedName("body")
  lateinit var body: String
}
