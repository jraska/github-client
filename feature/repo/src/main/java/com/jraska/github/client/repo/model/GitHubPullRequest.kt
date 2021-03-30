package com.jraska.github.client.repo.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
internal class GitHubPullRequest {

  @SerializedName("title")
  lateinit var title: String

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

  @SerializedName("additions")
  var additions = 0

  @SerializedName("deletions")
  var deletions = 0

  @SerializedName("changed_files")
  var changedFiles = 0

  @SerializedName("comments")
  var comments = 0

  @SerializedName("review_comments")
  var reviewComments = 0

  @SerializedName("commits")
  var commits = 0
}
