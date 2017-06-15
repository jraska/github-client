package com.jraska.github.client.users;

import com.jraska.github.client.Navigator;
import com.jraska.github.client.analytics.EventAnalytics;
import okhttp3.HttpUrl;
import org.junit.Test;

import static com.jraska.github.client.users.UserDetailViewModelTest.trampoline;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RepoDetailViewModelTest {
  @Test
  public void whenFullNameClicked_thenLaunchedOnWeb() {
    Navigator navigatorMock = mock(Navigator.class);

    RepoDetailViewModel viewModel = new RepoDetailViewModel(mock(UsersRepository.class),
      trampoline(), navigatorMock, EventAnalytics.EMPTY);

    viewModel.onFitHubIconClicked("jraska/Falcon");

    verify(navigatorMock).launchOnWeb(HttpUrl.parse("https://github.com/jraska/Falcon"));
  }
}
