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

  @SerializedName("body")
  var body: String = ""
}

class IssueDto {
  @SerializedName("pull_request")
  var pullRequest: PullRequestDto? = null

  @SerializedName("number")
  var number: Int = 0
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
