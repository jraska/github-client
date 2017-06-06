package com.jraska.github.client.users;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.jraska.github.client.Navigator;
import com.jraska.github.client.Urls;
import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.rx.AppSchedulers;
import com.jraska.github.client.rx.RxLiveData;
import com.jraska.github.client.users.UserDetail;
import com.jraska.github.client.users.UsersRepository;
import io.reactivex.Observable;

public class UserDetailViewModel extends ViewModel {
  private final UsersRepository usersRepository;
  private final AppSchedulers schedulers;
  private final Navigator navigator;
  private final EventAnalytics eventAnalytics;

  UserDetailViewModel(UsersRepository usersRepository, AppSchedulers schedulers,
                             Navigator navigator, EventAnalytics eventAnalytics) {
    this.usersRepository = usersRepository;
    this.schedulers = schedulers;
    this.navigator = navigator;
    this.eventAnalytics = eventAnalytics;
  }

  // TODO: 06/06/17 Cache observable for same login
  public LiveData<UserDetail> userDetail(String login) {
    Observable<UserDetail> detailObservable = usersRepository.getUserDetail(login)
      .subscribeOn(schedulers.io())
      .observeOn(schedulers.mainThread());

    return RxLiveData.adapt(detailObservable);
  }

  public void onUserGitHubIconClick(String login) {
    AnalyticsEvent event = AnalyticsEvent.builder("open_github_from_detail")
      .addProperty("login", login)
      .build();

    eventAnalytics.report(event);

    navigator.launchOnWeb(Urls.user(login));
  }
}
