package com.jraska.github.client.repo.model

import kotlinx.coroutines.flow.Flow

internal interface RepoRepository {
  fun getRepoDetail(owner: String, repoName: String): Flow<RepoDetail>
}
