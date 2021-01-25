package com.jraska.github.client.users.model

import org.threeten.bp.Instant

internal class UserDetailWithReposConverter {

  fun translateUserDetail(gitHubUserDetail: GitHubUserDetail, gitHubRepos: List<GitHubUserRepo>, reposToDisplay: Int): UserDetail {
    val joined = Instant.parse(gitHubUserDetail.createdAt)

    val stats = UserStats(
      gitHubUserDetail.followers!!, gitHubUserDetail.following!!,
      gitHubUserDetail.publicRepos!!, joined
    )

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

  private fun convert(gitHubRepo: GitHubUserRepo): RepoHeader {
    return RepoHeader(
      gitHubRepo.owner!!.login!!,
      gitHubRepo.name!!, gitHubRepo.description ?: "",
      gitHubRepo.stargazersCount!!, gitHubRepo.forks!!
    )
  }

  private fun convert(gitHubUser: GitHubUserDetail): User {
    val isAdmin = gitHubUser.siteAdmin ?: false
    return User(gitHubUser.login!!, gitHubUser.avatarUrl!!, isAdmin)
  }

  companion object {
    val INSTANCE = UserDetailWithReposConverter()
  }
}
