package com.jraska.github.client.repo.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
internal class GitHubRepoUser {

    @SerializedName("login")
    @Expose
    var login: String? = null
}
