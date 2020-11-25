package com.jraska.devanalytics.github.model

data class GitHubPrEvent(
  val action: String,
  val author: String,
  val prUrl: String,
  val prNumber: Int,
  val comment: String?,
  val state: String?
)
