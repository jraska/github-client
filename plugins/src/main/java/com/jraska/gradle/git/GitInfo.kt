package com.jraska.gradle.git

class GitInfo(
  val branchName: String,
  val commitId: String,
  val dirty: Boolean,
  val status: String
) {
  fun asAnalyticsProperties(): Map<String, Any?> {
    return mapOf(
      "gitBranch" to branchName,
      "gitCommit" to commitId,
      "gitDirty" to dirty,
      "gitStatus" to status,
    )
  }
}
