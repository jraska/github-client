package com.jraska.github.client.users

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.Collections

internal class GitHubApiUsersRepository(
  private val gitHubUsersApi: GitHubUsersApi,
  private val gitHubUserDetailApi: GitHubUserDetailApi
) : UsersRepository {
  private val converter: UserDetailWithReposConverter = UserDetailWithReposConverter.INSTANCE

  private var lastUsers: List<User>? = null

  override fun getUsers(since: Int): Single<List<User>> {
    return gitHubUsersApi.getUsers(since)
      .map { this.translateUsers(it) }
      .doOnSuccess { users -> lastUsers = users }
  }

  override fun getUserDetail(login: String, reposInSection: Int): Observable<UserDetail> {
    return gitHubUserDetailApi.getUserDetail(login)
      .subscribeOn(Schedulers.io()) // this has to be here now to run requests in parallel
      .zipWith(gitHubUserDetailApi.getRepos(login), BiFunction { a: GitHubUserDetail, b: List<GitHubRepo> -> Pair(a, b) })
      .map { result -> converter.translateUserDetail(result.component1(), result.component2(), reposInSection) }
      .toObservable()
      .startWith(cachedUser(login))
  }

  override fun getRepoDetail(owner: String, repoName: String): Observable<RepoDetail> {
    return gitHubUsersApi.getRepo(owner, repoName)
      .toObservable()
      .map { RepoConverter.INSTANCE.convertToDetail(it) }
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
