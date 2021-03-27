package com.jraska.gradle

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

class CiInfo(
  val runId: Long,
  val workflowName: String,
  val runNumber: Int,
  val runLink: HttpUrl
) {
  fun asAnalyticsProperties(): Map<String, Any?> {
    return mapOf(
      "runId" to runId,
      "workflowName" to workflowName,
      "runNumber" to runNumber,
      "runLink" to runLink.toString(),
    )
  }

  companion object {
    fun collectGitHubActions(): CiInfo? {
      val workflowName = System.getenv("GITHUB_WORKFLOW") ?: return null

      val runId = System.getenv("GITHUB_RUN_ID").toLong()
      val runNumber = System.getenv("GITHUB_RUN_NUMBER").toInt()
      val runLink = "https://github.com/jraska/github-client/actions/runs/$runId".toHttpUrl()

      return CiInfo(runId, workflowName, runNumber, runLink)
    }
  }
}
