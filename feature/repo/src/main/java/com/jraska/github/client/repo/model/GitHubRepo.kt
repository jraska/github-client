package com.jraska.github.client.repo.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
internal class GitHubRepo {
  @SerializedName("id")
  @Expose
  var id: Int? = null
  @SerializedName("name")
  @Expose
  var name: String? = null
  @SerializedName("full_name")
  @Expose
  var fullName: String? = null
  @SerializedName("owner")
  @Expose
  var owner: GitHubRepoUser? = null
  @SerializedName("private")
  @Expose
  var private: Boolean? = null
  @SerializedName("description")
  @Expose
  var description: String? = null
  @SerializedName("fork")
  @Expose
  var fork: Boolean? = null
  @SerializedName("url")
  @Expose
  var url: String? = null
  @SerializedName("teams_url")
  @Expose
  var teamsUrl: String? = null
  @SerializedName("statuses_url")
  @Expose
  var statusesUrl: String? = null
  @SerializedName("subscribers_url")
  @Expose
  var subscribersUrl: String? = null
  @SerializedName("issues_url")
  @Expose
  var issuesUrl: String? = null
  @SerializedName("pulls_url")
  @Expose
  var pullsUrl: String? = null
  @SerializedName("milestones_url")
  @Expose
  var milestonesUrl: String? = null
  @SerializedName("labels_url")
  @Expose
  var labelsUrl: String? = null
  @SerializedName("releases_url")
  @Expose
  var releasesUrl: String? = null
  @SerializedName("created_at")
  @Expose
  var createdAt: String? = null
  @SerializedName("updated_at")
  @Expose
  var updatedAt: String? = null
  @SerializedName("pushed_at")
  @Expose
  var pushedAt: String? = null
  @SerializedName("git_url")
  @Expose
  var gitUrl: String? = null
  @SerializedName("size")
  @Expose
  var size: Int? = null
  @SerializedName("stargazers_count")
  @Expose
  var stargazersCount: Int? = null
  @SerializedName("watchers_count")
  @Expose
  var watchersCount: Int? = null
  @SerializedName("language")
  @Expose
  var language: String? = null
  @SerializedName("forks_count")
  @Expose
  var forksCount: Int? = null
  @SerializedName("open_issues_count")
  @Expose
  var openIssuesCount: Int? = null
  @SerializedName("forks")
  @Expose
  var forks: Int? = null
  @SerializedName("open_issues")
  @Expose
  var openIssues: Int? = null
  @SerializedName("watchers")
  @Expose
  var watchers: Int? = null
  @SerializedName("default_branch")
  @Expose
  var defaultBranch: String? = null
  @SerializedName("subscribers_count")
  @Expose
  var subscribersCount: Int? = null
}
