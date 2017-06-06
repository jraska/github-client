package com.jraska.github.client.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.jraska.github.client.Navigator;
import com.jraska.github.client.Urls;
import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.rx.AppSchedulers;
import com.jraska.github.client.rx.RxLiveData;
import com.jraska.github.client.users.User;
import com.jraska.github.client.users.UsersRepository;
import io.reactivex.Single;

import java.util.List;

public class UsersViewModel extends ViewModel{

  private final UsersRepository usersRepository;
  private final AppSchedulers appSchedulers;
  private final Navigator navigator;
  private final EventAnalytics eventAnalytics;

  private final RxLiveData.SingleAdapter<List<User>> users;

  public UsersViewModel(UsersRepository usersRepository, AppSchedulers appSchedulers,
                        Navigator navigator, EventAnalytics eventAnalytics) {
    this.usersRepository = usersRepository;
    this.appSchedulers = appSchedulers;
    this.navigator = navigator;
    this.eventAnalytics = eventAnalytics;

    users = RxLiveData.adapt(usersInternal());
  }

  LiveData<List<User>> users() {
    return users;
  }

  public void onRefresh() {
    users.resubscribe();
  }

  private Single<List<User>> usersInternal() {
    return usersRepository.getUsers(0)
      .subscribeOn(appSchedulers.io())
      .observeOn(appSchedulers.mainThread());
  }

  public void onUserClicked(User user) {
    AnalyticsEvent event = AnalyticsEvent.builder("open_user_detail")
      .addProperty("login", user.login)
      .build();

    eventAnalytics.report(event);

    navigator.startUserDetail(user.login);
  }

  public void onUserGitHubIconClicked(User user) {
    AnalyticsEvent event = AnalyticsEvent.builder("open_github_from_list")
      .addProperty("login", user.login)
      .build();

    eventAnalytics.report(event);

    navigator.launchOnWeb(Urls.user(user.login));
  }
}
