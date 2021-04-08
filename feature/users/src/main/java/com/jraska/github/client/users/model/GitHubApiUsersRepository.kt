package com.jraska.github.client.users.model

import com.jraska.github.client.rx.AppSchedulers
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.Collections

internal class GitHubApiUsersRepository(
  private val gitHubUsersApi: GitHubUsersApi,
  private val appSchedulers: AppSchedulers
) : UsersRepository {
  private val converter: UserDetailWithReposConverter =
    UserDetailWithReposConverter.INSTANCE

  private var lastUsers: List<User>? = null

  override fun getUsers(since: Int): Single<List<User>> {
    return gitHubUsersApi.getUsers(since)
      .map { this.translateUsers(it) }
      .doOnSuccess { users -> lastUsers = users }
  }

  override fun getUserDetail(login: String, reposInSection: Int): Observable<UserDetail> {
    return gitHubUsersApi.getUserDetail(login)
      .subscribeOn(appSchedulers.io) // this has to be here now to run requests in parallel
      .zipWith(gitHubUsersApi.getRepos(login), { a: GitHubUserDetail, b: List<GitHubUserRepo> -> Pair(a, b) })
      .map { result -> converter.translateUserDetail(result.component1(), result.component2(), reposInSection) }
      .toObservable()
      .startWith(cachedUser(login))
  }

  private fun cachedUser(login: String): Observable<UserDetail> {
    val lastUsers = this.lastUsers ?: return Observable.empty()

    return lastUsers.firstOrNull { login == it.login }
      ?.let { Observable.just(UserDetail(it, null, emptyList(), emptyList())) }
      ?: Observable.empty()
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
