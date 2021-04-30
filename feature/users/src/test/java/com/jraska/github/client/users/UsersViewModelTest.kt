package com.jraska.github.client.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.github.client.http.MockWebServerInterceptor
import com.jraska.github.client.http.MockWebServerInterceptorRule
import com.jraska.github.client.http.enqueue
import com.jraska.github.client.users.di.DaggerTestUsersComponent
import com.jraska.livedata.test
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UsersViewModelTest {
  @get:Rule val testRule = InstantTaskExecutorRule()

  @get:Rule val mockWebServer = MockWebServer()

  @get:Rule val mockWebServerInterceptorRule = MockWebServerInterceptorRule(mockWebServer)

  private lateinit var viewModel: UsersViewModel

  @Before
  fun setUp() {
    val component = DaggerTestUsersComponent.create()
    viewModel = component.usersViewModel()

    mockWebServer.enqueue("response/users.json")
    mockWebServer.enqueue("response/users_with_extra.json")
  }

  @Test
  fun refreshesProperly() {
    val testObserver = viewModel.users()
      .test()
      .assertValue { it is UsersViewModel.ViewState.ShowUsers }
      .assertHistorySize(2)

    testObserver.assertValue { it is UsersViewModel.ViewState.ShowUsers }

    viewModel.onRefresh()

    testObserver.assertHistorySize(4)

    viewModel.users()
      .test()
      .assertNever { it == null }
      .map { it as UsersViewModel.ViewState.ShowUsers }
      .assertValue { it.users.last().login == "jraska" }
  }
}
