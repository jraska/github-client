package com.jraska.github.client.repo.model

import io.reactivex.rxjava3.core.Observable

internal interface RepoRepository {
  fun getRepoDetail(owner: String, repoName: String): Observable<RepoDetail>
}
