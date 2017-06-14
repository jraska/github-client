package com.jraska.github.client.users;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.jraska.github.client.Navigator;
import com.jraska.github.client.Urls;
import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.rx.AppSchedulers;
import com.jraska.github.client.rx.RxLiveData;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class UserDetailViewModel extends ViewModel {
  private final UsersRepository usersRepository;
  private final AppSchedulers schedulers;
  private final Navigator navigator;
  private final EventAnalytics eventAnalytics;

  private final Map<String, RxLiveData<ViewState>> liveDataMapping = new HashMap<>();

  UserDetailViewModel(UsersRepository usersRepository, AppSchedulers schedulers,
                      Navigator navigator, EventAnalytics eventAnalytics) {
    this.usersRepository = usersRepository;
    this.schedulers = schedulers;
    this.navigator = navigator;
    this.eventAnalytics = eventAnalytics;
  }

  public LiveData<ViewState> userDetail(String login) {
    RxLiveData<ViewState> liveData = liveDataMapping.get(login);
    if (liveData == null) {
      liveData = newUserLiveData(login);
      liveDataMapping.put(login, liveData);
    }

    return liveData;
  }

  private RxLiveData<ViewState> newUserLiveData(String login) {
    Observable<ViewState> viewStateObservable = usersRepository.getUserDetail(login)
      .subscribeOn(schedulers.io())
      .observeOn(schedulers.mainThread())
      .map((userDetail -> new ViewState(null, userDetail)))
      .onErrorReturn((error) -> new ViewState(error, null))
      .startWith(new ViewState(null, null));

    return RxLiveData.from(viewStateObservable);
  }

  public void onUserGitHubIconClick(String login) {
    AnalyticsEvent event = AnalyticsEvent.builder("open_github_from_detail")
      .addProperty("login", login)
      .build();

    eventAnalytics.report(event);

    navigator.launchOnWeb(Urls.user(login));
  }

  public void onRepoClicked(RepoHeader header) {
    navigator.startRepoDetail(header.fullName());
  }

  public static class ViewState {
    private final Throwable error;
    private final UserDetail result;

    ViewState(Throwable error, UserDetail result) {
      this.error = error;
      this.result = result;
    }

    public boolean isLoading() {
      return (result == null || result.basicStats == null) && error == null;
    }

    public Throwable error() {
      return error;
    }

    public UserDetail result() {
      return result;
    }
  }
}
