package com.jraska.github.client.users.model

import kotlinx.coroutines.flow.Flow

internal interface UsersRepository {
  suspend fun getUsers(since: Int): List<User>

  fun getUserDetail(login: String, reposInSection: Int): Flow<UserDetail>
}
