package com.jraska.github.client.users.data

import com.jraska.github.client.users.RepoHeader
import com.jraska.github.client.users.User
import com.jraska.github.client.users.UserDetail
import com.jraska.github.client.users.UserStats
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

internal class UserDetailWithReposConverter {

  fun translateUserDetail(gitHubUserDetail: GitHubUserDetail, gitHubRepos: List<GitHubRepo>, reposToDisplay: Int): UserDetail {
    val joined = LocalDateTime.parse(gitHubUserDetail.createdAt, DateTimeFormatter.ISO_DATE_TIME)

    val stats = UserStats(gitHubUserDetail.followers!!, gitHubUserDetail.following!!,
      gitHubUserDetail.publicRepos!!, joined)

    val sortedRepos = gitHubRepos.sortedWith(compareByDescending { it.stargazersCount })

    val usersRepos = ArrayList<RepoHeader>()
    val contributedRepos = ArrayList<RepoHeader>()

    for (gitHubRepo in sortedRepos) {
      if (usersRepos.size < reposToDisplay && gitHubUserDetail.login == gitHubRepo.owner!!.login) {
        val repo = convert(gitHubRepo)
        usersRepos.add(repo)
      } else if (contributedRepos.size < reposToDisplay) {
        val repo = convert(gitHubRepo)
        contributedRepos.add(repo)
      }
    }

    val user = convert(gitHubUserDetail)
    return UserDetail(user, stats, usersRepos, contributedRepos)
  }

  fun convert(gitHubRepo: GitHubRepo): RepoHeader {
    return RepoConverter.INSTANCE.convert(gitHubRepo)
  }

  private fun convert(gitHubUser: GitHubUserDetail): User {
    val isAdmin = gitHubUser.siteAdmin ?: false
    return User(gitHubUser.login!!, gitHubUser.avatarUrl!!, isAdmin, gitHubUser.htmlUrl!!)
  }

  companion object {
    val INSTANCE = UserDetailWithReposConverter()
  }
}
