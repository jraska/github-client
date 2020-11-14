package com.jraska.gradle.git

class GitInfo(
  val branchName: String,
  val commitId: String,
  val dirty: Boolean,
  val status: String
)
