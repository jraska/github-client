package com.jraska.github.client.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.github.client.http.enqueue
import com.jraska.github.client.http.onUrlPartReturn
import com.jraska.github.client.http.onUrlReturn
import com.jraska.github.client.users.di.DaggerTestUsersComponent
import com.jraska.github.client.users.model.RepoHeader
import com.jraska.github.client.users.model.User
import com.jraska.github.client.users.model.UserDetail
import com.jraska.github.client.users.model.UserStats
import com.jraska.livedata.test
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.Instant

class UserDetailViewModelTest {
  @get:Rule
  val testRule = InstantTaskExecutorRule()

  private lateinit var viewModel: UserDetailViewModel
  private lateinit var mockWebServer: MockWebServer

  @Before
  fun before() {
    val component = DaggerTestUsersComponent.create()
    viewModel = component.userDetailViewModel()
    mockWebServer = component.mockWebServer
  }

  @Test
  fun whenLiveDataThenCorrectUser() {
    mockWebServer.onUrlReturn(".*/users/jraska".toRegex(), "response/jraska.json")
    mockWebServer.onUrlPartReturn("users/jraska/repos", "response/jraska_repos.json")

    val observer = viewModel.userDetail("jraska")
      .test()

//    Thread.sleep(200)

    val displayUser = observer.value() as UserDetailViewModel.ViewState.DisplayUser

    assertThat(displayUser.user).usingRecursiveComparison().isEqualTo(testDetail())
  }

  @Test
  fun whenErrorThenErrorStateDisplayed() {
    mockWebServer.enqueue("response/error.json")
    mockWebServer.enqueue("response/error.json")

    val value = viewModel.userDetail("jraska").test().value()

    assertThat(value).isInstanceOf(UserDetailViewModel.ViewState.Error::class.java)
  }

  @Test
  fun whenSameLoginMultipleTimesThenOnlyOneObservableCreated() {
    mockWebServer.onUrlReturn(".*/users/mojombo".toRegex(), "response/mojombo.json")
    mockWebServer.onUrlPartReturn("users/mojombo/repos", "response/mojombo_repos.json")

    mockWebServer.onUrlReturn(".*/users/jraska".toRegex(), "response/jraska.json")
    mockWebServer.onUrlPartReturn("users/jraska/repos", "response/jraska_repos.json")

    viewModel.userDetail("mojombo").test()
    viewModel.userDetail("mojombo").test()

    assertThat(mockWebServer.requestCount).isEqualTo(2)

    viewModel.userDetail("jraska").test()
    assertThat(mockWebServer.requestCount).isEqualTo(4)
  }

  companion object {
    internal fun testDetail(): UserDetail {
      val user = User("jraska", "https://avatars.githubusercontent.com/u/6277721?v=4", false)
      val stats = UserStats(74, 2, 17, Instant.parse("2013-12-28T19:43:19Z"))

      val repos = listOf(
        RepoHeader("jraska", "Console", "https://github.com/jraska/Console", 107, 12),
        RepoHeader("jraska", "Dagger-Codelab", "https://github.com/jraska/Dagger-Codelab", 8, 5),
        RepoHeader("jraska", "TimerViews", "https://github.com/jraska/TimerViews", 2, 3),
      )

      return UserDetail(user, stats, repos, emptyList())
    }
  }
}
