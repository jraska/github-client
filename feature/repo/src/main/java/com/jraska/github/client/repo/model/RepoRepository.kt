package com.jraska.github.client.repo.model

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

internal interface RepoRepository {
  fun getRepoDetail(owner: String, repoName: String): Observable<RepoDetail>
}
