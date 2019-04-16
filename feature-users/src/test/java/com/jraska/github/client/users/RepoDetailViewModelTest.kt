package com.jraska.github.client.users

import com.jraska.github.client.Navigator
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.users.model.RepoDetailViewModel
import com.jraska.github.client.users.model.UsersRepository
import okhttp3.HttpUrl
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class RepoDetailViewModelTest {
  @Test
  fun whenFullNameClicked_thenLaunchedOnWeb() {
    val navigatorMock = mock(Navigator::class.java)

    val viewModel = RepoDetailViewModel(
      mock(UsersRepository::class.java),
      trampoline(), navigatorMock, EventAnalytics.EMPTY
    )

    viewModel.onFitHubIconClicked("jraska/Falcon")

    verify(navigatorMock).launchOnWeb(HttpUrl.get("https://github.com/jraska/Falcon"))
  }
}
