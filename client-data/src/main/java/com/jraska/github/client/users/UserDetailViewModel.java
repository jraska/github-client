package com.jraska.github.client.users;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.jraska.github.client.Navigator;
import com.jraska.github.client.Urls;
import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.rx.AppSchedulers;
import com.jraska.github.client.rx.RxLiveData;
import io.reactivex.Observable;

import java.util.HashMap;
import java.util.Map;

public class UserDetailViewModel extends ViewModel {
  private final UsersRepository usersRepository;
  private final AppSchedulers schedulers;
  private final Navigator navigator;
  private final EventAnalytics eventAnalytics;

  private final Map<String, LiveData<UserDetail>> liveDataMapping = new HashMap<>();

  UserDetailViewModel(UsersRepository usersRepository, AppSchedulers schedulers,
                      Navigator navigator, EventAnalytics eventAnalytics) {
    this.usersRepository = usersRepository;
    this.schedulers = schedulers;
    this.navigator = navigator;
    this.eventAnalytics = eventAnalytics;
  }

  public LiveData<UserDetail> userDetail(String login) {
    LiveData<UserDetail> liveData = liveDataMapping.get(login);
    if (liveData == null) {
      liveData = newUserLiveData(login);
      liveDataMapping.put(login, liveData);
    }

    return liveData;
  }

  private LiveData<UserDetail> newUserLiveData(String login) {
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
