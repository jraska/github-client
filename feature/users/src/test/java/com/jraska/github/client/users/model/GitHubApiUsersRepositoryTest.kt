package com.jraska.github.client.users.model

import com.jraska.github.client.Fakes
import com.jraska.github.client.http.HttpTest
import com.jraska.github.client.http.enqueue
import com.jraska.github.client.http.onUrlPartReturn
import com.jraska.github.client.http.onUrlReturn
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.runBlocking
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
    repository = GitHubApiUsersRepository(HttpTest.retrofit(mockWebServer).create(GitHubUsersApi::class.java), Fakes.unconfined())
  }

  @Test
  fun getsUsersProperly() {
    mockWebServer.enqueue("response/users.json")

    val users = runBlocking { repository.getUsers(0) }

    assertThat(users).hasSize(30)
    assertThat(users.first()).usingRecursiveComparison().isEqualTo(testFirstUser())
  }

  @Test
  fun cachesUserProperly() {
    mockWebServer.onUrlPartReturn("users?since", "response/users.json")
    mockWebServer.onUrlReturn(".*/users.mojombo".toRegex(), "response/mojombo.json")
    mockWebServer.onUrlPartReturn("users/mojombo/repos", "response/mojombo_repos.json")

    val values = runBlocking {
      val userDetails = repository.getUserDetail("mojombo", 1).toCollection(mutableListOf())
      assertThat(userDetails.size).isEqualTo(1)

      repository.getUsers(0)

      repository.getUserDetail("mojombo", 1).toCollection(mutableListOf())
    }

    assertThat(values.size).isEqualTo(2)
    assertThat(values.first().user).usingRecursiveComparison().isEqualTo(testFirstUser())
    assertThat(values.last().user).usingRecursiveComparison().isEqualTo(testFirstUser())
  }

  private fun testFirstUser(): User {
    return User("mojombo", "https://avatars.githubusercontent.com/u/1?v=4", false)
  }
}
