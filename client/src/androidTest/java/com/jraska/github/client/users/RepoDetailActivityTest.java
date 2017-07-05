package com.jraska.github.client.users;

import android.support.test.espresso.action.ViewActions;

import com.jraska.github.client.AppModule;
import com.jraska.github.client.DeepLinkLaunchTest;
import com.jraska.github.client.Navigator;
import com.jraska.github.client.R;
import com.jraska.github.client.ViewModelFactoryDecorator;
import com.jraska.github.client.analytics.EventAnalytics;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import okhttp3.HttpUrl;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepoDetailActivityTest {
  @Mock Navigator navigatorMock;
  @Mock UsersRepository repositoryMock;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);

    when(repositoryMock.getRepoDetail(any(), any())).thenReturn(Observable.empty());
  }

  @Test
  public void whenFabClicked_thenNavigatesToGitHub() {
    RepoDetailViewModel detailViewModel = new RepoDetailViewModel(repositoryMock,
      AppModule.schedulers(), navigatorMock, EventAnalytics.EMPTY);
    ViewModelFactoryDecorator.setToApp(RepoDetailViewModel.class, detailViewModel);

    String deepLink = "https://github.com/jraska/Falcon";
    DeepLinkLaunchTest.launchDeepLink(deepLink);

    onView(withId(R.id.repo_detail_github_fab)).perform(ViewActions.click());
    verify(navigatorMock).launchOnWeb(HttpUrl.parse(deepLink));
  }
}
