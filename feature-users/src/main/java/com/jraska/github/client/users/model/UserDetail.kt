package com.jraska.github.client.users.model

import java.util.Collections

internal class UserDetail(
  val user: User,
  val basicStats: UserStats?,
  popularRepos: List<RepoHeader>,
  contributedRepos: List<RepoHeader>
) {

  val popularRepos: List<RepoHeader> = Collections.unmodifiableList(popularRepos)
  val contributedRepos: List<RepoHeader> = Collections.unmodifiableList(contributedRepos)
}
