package com.jraska.github.client.users.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
internal class GitHubUserRepo {
  @SerializedName("id")
  @Expose
  var id: Int? = null
  @SerializedName("name")
  @Expose
  var name: String? = null
  @SerializedName("owner")
  @Expose
  var owner: GitHubUser? = null
  @SerializedName("html_url")
  @Expose
  var description: String? = null
  @SerializedName("stargazers_count")
  @Expose
  var stargazersCount: Int? = null
  @SerializedName("forks")
  @Expose
  var forks: Int? = null
}
