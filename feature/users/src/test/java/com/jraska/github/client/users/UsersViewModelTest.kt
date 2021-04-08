package com.jraska.github.client.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.github.client.Fakes
import com.jraska.github.client.http.enqueue
import com.jraska.github.client.navigation.Navigator
import com.jraska.github.client.users.di.DaggerTestUsersComponent
import com.jraska.github.client.users.model.GitHubApiUsersRepository
import com.jraska.github.client.users.model.GitHubUser
import com.jraska.github.client.users.model.GitHubUsersApi
import com.jraska.livedata.test
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UsersViewModelTest {
  @get:Rule val testRule = InstantTaskExecutorRule()

  private lateinit var viewModel: UsersViewModel

  @Before
  fun setUp() {
    val component = DaggerTestUsersComponent.create()
    viewModel = component.usersViewModel()

    component.mockWebServer().enqueue("response/users.json")
    component.mockWebServer().enqueue("response/users_with_extra.json")
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
