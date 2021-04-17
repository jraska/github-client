package com.jraska.github.client.users.model

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

internal interface UsersRepository {
  fun getUsers(since: Int): Single<List<User>>

  fun getUserDetail(login: String, reposInSection: Int): Observable<UserDetail>
}
