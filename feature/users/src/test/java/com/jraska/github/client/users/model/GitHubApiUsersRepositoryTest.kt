package com.jraska.github.client.users.model

import com.jraska.github.client.Fakes
import com.jraska.github.client.http.HttpTest
import com.jraska.github.client.http.enqueue
import com.jraska.github.client.http.onUrlPartReturn
import com.jraska.github.client.http.onUrlReturn
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GitHubApiUsersRepositoryTest {

  @get:Rule
  val mockWebServer = MockWebServer()

  internal lateinit var repository: GitHubApiUsersRepository

  @Before
  fun setUp() {
    repository = GitHubApiUsersRepository(HttpTest.retrofit(mockWebServer).create(GitHubUsersApi::class.java), Fakes.trampoline())
  }

  @Test
  fun getsUsersProperly() {
    mockWebServer.enqueue("response/users.json")

    val users = repository.getUsers(0)
      .test()
      .values()
      .first()

    assertThat(users).hasSize(30)
    assertThat(users.first()).usingRecursiveComparison().isEqualTo(testFirstUser())
  }

  @Test
  fun cachesUserProperly() {
    mockWebServer.onUrlPartReturn("users?since", "response/users.json")
    mockWebServer.onUrlReturn(".*/users.mojombo".toRegex(), "response/mojombo.json")
    mockWebServer.onUrlPartReturn("users/mojombo/repos", "response/mojombo_repos.json")

    repository.getUserDetail("mojombo", 1)
      .test()
      .awaitCount(1)
      .assertComplete()

    repository.getUsers(0).test() // this line simulates the list request to cache
    val values = repository.getUserDetail("mojombo", 1)
      .test()
      .awaitCount(2)
      .assertComplete()
      .values()

    assertThat(values.first().user).usingRecursiveComparison().isEqualTo(testFirstUser())
    assertThat(values.last().user).usingRecursiveComparison().isEqualTo(testFirstUser())
  }

  private fun testFirstUser(): User {
    return User("mojombo", "https://avatars.githubusercontent.com/u/1?v=4", false)
  }
}
