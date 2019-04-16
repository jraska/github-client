package com.jraska.github.client.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.github.client.Navigator
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.users.model.GitHubApiUsersRepository
import com.jraska.github.client.users.model.GitHubUser
import com.jraska.github.client.users.model.GitHubUserDetailApi
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

  @Mock private lateinit var detailApi: GitHubUserDetailApi
  @Mock private lateinit var usersApi: GitHubUsersApi
  @Mock private lateinit var navigator: Navigator

  private lateinit var viewModel: UsersViewModel
  private var users = ArrayList<GitHubUser>()

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)

    Mockito.`when`(usersApi.getUsers(0)).thenReturn(Single.fromCallable({ users }))

    val usersRepository = GitHubApiUsersRepository(usersApi, detailApi)
    viewModel = UsersViewModel(usersRepository, trampoline(), navigator, EventAnalytics.EMPTY)
  }

  @Test
  fun refreshesProperly() {
    val testObserver = viewModel.users()
      .test()
      .assertValue { it is UsersViewModel.ViewState.ShowUsers }
      .assertHistorySize(2)

    users.add(GitHubUser().apply {
      login = "jraska"
      avatarUrl = "https://gihub.com/jraska/avatar.png"
      htmlUrl = "https://github.com/jraska"
    })

    testObserver.assertValue { it is UsersViewModel.ViewState.ShowUsers }

    viewModel.onRefresh()

    testObserver.assertHistorySize(4)

    viewModel.users()
      .test()
      .assertNever { it == null }
      .map { it as UsersViewModel.ViewState.ShowUsers }
      .assertValue { it.users.first().login == "jraska" }
  }
}
