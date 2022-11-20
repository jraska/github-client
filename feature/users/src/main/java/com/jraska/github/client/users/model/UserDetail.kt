package com.jraska.github.client.users.model

internal class UserDetail(
  val user: User,
  val basicStats: UserStats?,
  val popularRepos: List<RepoHeader>,
  val contributedRepos: List<RepoHeader>
)
