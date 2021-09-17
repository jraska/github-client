package com.jraska.github.client.users.model

import com.jraska.github.client.coroutines.AppDispatchers
import com.jraska.github.client.coroutines.result
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.Collections

internal class GitHubApiUsersRepository(
  private val gitHubUsersApi: GitHubUsersApi,
  private val appDispatchers: AppDispatchers
) : UsersRepository {
  private val converter: UserDetailWithReposConverter =
    UserDetailWithReposConverter.INSTANCE

  private var lastUsers: List<User>? = null

  override suspend fun getUsers(since: Int): List<User> {
    val userDtos = gitHubUsersApi.getUsers(since).result()
    return translateUsers(userDtos).also { lastUsers = it }
  }

  override fun getUserDetail(login: String, reposInSection: Int): Flow<UserDetail> {
    return flow {
      cachedUser(login)?.let {
        emit(it)
      }

      val userDetail = withContext(appDispatchers.io) {
        val asyncDetail = async { gitHubUsersApi.getUserDetail(login).result() }
        val asyncRepo = async { gitHubUsersApi.getRepos(login).result() }
        awaitAll(asyncDetail, asyncRepo)

        converter.translateUserDetail(asyncDetail.await(), asyncRepo.await(), reposInSection)
      }

      emit(userDetail)
    }
  }

  private fun cachedUser(login: String): UserDetail? {
    val lastUsers = this.lastUsers ?: return null

    return lastUsers.firstOrNull { login == it.login }
      ?.let { UserDetail(it, null, emptyList(), emptyList()) }
  }

  private fun translateUsers(gitHubUsers: List<GitHubUser>): List<User> {
    val users = gitHubUsers.map { convert(it) }

    return Collections.unmodifiableList(users)
  }

  private fun convert(gitHubUser: GitHubUser): User {
    val isAdmin = gitHubUser.siteAdmin ?: false
    return User(gitHubUser.login!!, gitHubUser.avatarUrl!!, isAdmin)
  }
}
