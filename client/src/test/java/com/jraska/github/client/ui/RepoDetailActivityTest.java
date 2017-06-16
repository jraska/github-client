package com.jraska.github.client.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import com.jraska.github.client.BuildConfig;
import com.jraska.github.client.Navigator;
import com.jraska.github.client.R;
import com.jraska.github.client.ViewModelFactoryDecorator;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.rx.AppSchedulers;
import com.jraska.github.client.users.RepoDetailViewModel;
import com.jraska.github.client.users.UserViewModelModule;
import com.jraska.github.client.users.UsersRepository;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RepoDetailActivityTest {

  @Captor ArgumentCaptor<Intent> intentCaptor;
  @Mock Activity activityMock;
  @Mock UsersRepository repositoryMock;
  @Mock Navigator navigatorMock;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    when(repositoryMock.getRepoDetail(any(), any())).thenReturn(Observable.empty());

    ViewModel viewModel = UserViewModelModule.provideRepoDetailModel(repositoryMock, trampoline(),
      navigatorMock, EventAnalytics.EMPTY);

    ViewModelFactoryDecorator.setToApp(RepoDetailViewModel.class, viewModel);
  }

  @Test
  public void whenFabClicked_thenNavigatesToGitHub() {
    RepoDetailActivity.start(activityMock, "jraska/Falcon");
    verify(activityMock).startActivity(intentCaptor.capture());

    RepoDetailActivity activity = Robolectric.buildActivity(RepoDetailActivity.class,
      intentCaptor.getValue()).setup().get();

    activity.findViewById(R.id.repo_detail_github_fab).performClick();

    verify(navigatorMock).launchOnWeb(HttpUrl.parse("https://github.com/jraska/Falconn"));
  }

  public static AppSchedulers trampoline() {
    return new AppSchedulers(Schedulers.trampoline(), Schedulers.trampoline(),
      Schedulers.trampoline());
  }
}
