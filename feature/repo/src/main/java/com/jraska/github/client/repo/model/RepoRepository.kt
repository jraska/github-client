package com.jraska.github.client.repo.model

import io.reactivex.Observable
import io.reactivex.Single

internal interface RepoRepository {
  fun getRepoDetail(owner: String, repoName: String): Observable<RepoDetail>
}
