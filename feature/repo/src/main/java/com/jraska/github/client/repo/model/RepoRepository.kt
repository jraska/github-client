package com.jraska.github.client.repo.model

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

internal interface RepoRepository {
  fun getRepoDetail(owner: String, repoName: String): Observable<RepoDetail>

  fun getRepoDetailFlow(owner: String, repoName: String): Flow<RepoDetail>
}
