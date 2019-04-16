package com.jraska.github.client.users

internal class RepoHeader(
  val owner: String,
  val name: String,
  val description: String,
  val stars: Int,
  val forks: Int
) {

  fun fullName(): String {
    return "$owner/$name"
  }

  companion object {

    fun owner(fullName: String): String {
      return fullName.substring(0, fullName.indexOf("/"))
    }

    fun name(fullName: String): String {
      return fullName.substring(fullName.indexOf("/") + 1)
    }
  }
}
