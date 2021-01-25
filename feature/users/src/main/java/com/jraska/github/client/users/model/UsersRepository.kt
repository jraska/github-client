package com.jraska.github.client.users.model

import io.reactivex.Observable
import io.reactivex.Single

internal interface UsersRepository {
  fun getUsers(since: Int): Single<List<User>>

  fun getUserDetail(login: String, reposInSection: Int): Observable<UserDetail>
}
