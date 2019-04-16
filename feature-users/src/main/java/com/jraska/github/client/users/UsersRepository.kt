package com.jraska.github.client.users

import io.reactivex.Observable
import io.reactivex.Single

internal interface UsersRepository {
  fun getUsers(since: Int): Single<List<User>>

  fun getUserDetail(login: String, reposInSection: Int): Observable<UserDetail>

  fun getRepoDetail(owner: String, repoName: String): Observable<RepoDetail>
}
