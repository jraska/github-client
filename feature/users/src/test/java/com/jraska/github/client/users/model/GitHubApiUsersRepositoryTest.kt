package com.jraska.github.client.users.model

import com.jraska.github.client.HttpTest
import com.jraska.github.client.enqueue
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class GitHubApiUsersRepositoryTest {

  val mockWebServer = MockWebServer()
  internal lateinit var repository: GitHubApiUsersRepository

  @Before
  fun setUp() {
    repository = GitHubApiUsersRepository(HttpTest.retrofit(mockWebServer.url("/")).create(GitHubUsersApi::class.java))
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

  private fun testFirstUser(): User {
    return User("mojombo", "https://avatars0.githubusercontent.com/u/1?v=4", false)
  }
}
