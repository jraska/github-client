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

public class RepoDetailViewModel extends ViewModel {
  private final UsersRepository usersRepository;
  private final AppSchedulers appSchedulers;
  private final Navigator navigator;
  private final EventAnalytics eventAnalytics;

  private final Map<String, LiveData<ViewState>> repoDetailLiveDataMap = new HashMap<>();

  RepoDetailViewModel(UsersRepository usersRepository, AppSchedulers appSchedulers,
                      Navigator navigator, EventAnalytics eventAnalytics) {
    this.usersRepository = usersRepository;
    this.appSchedulers = appSchedulers;
    this.navigator = navigator;
    this.eventAnalytics = eventAnalytics;
  }

  public LiveData<ViewState> repoDetail(String fullRepoName) {
    LiveData<ViewState> liveData = repoDetailLiveDataMap.get(fullRepoName);
    if (liveData == null) {
      liveData = newRepoDetailLiveData(fullRepoName);
      repoDetailLiveDataMap.put(fullRepoName, liveData);
    }

    return liveData;
  }

  private LiveData<ViewState> newRepoDetailLiveData(String fullRepoName) {
    String[] parts = fullRepoName.split("/");
    Observable<ViewState> stateObservable = usersRepository.getRepoDetail(parts[0], parts[1])
      .subscribeOn(appSchedulers.io())
      .observeOn(appSchedulers.mainThread())
      .map((detail) -> new ViewState(detail, null))
      .onErrorReturn(throwable -> new ViewState(null, throwable));

    return RxLiveData.from(stateObservable);
  }

  public void onFitHubIconClicked(String fullRepoName) {
    AnalyticsEvent event = AnalyticsEvent.builder("open_repo_from_detail")
      .addProperty("owner", RepoHeader.owner(fullRepoName))
      .addProperty("name", RepoHeader.name(fullRepoName))
      .build();

    eventAnalytics.report(event);

    navigator.launchOnWeb(Urls.repo(fullRepoName));
  }

  public static class ViewState {
    public final RepoDetail repoDetail;
    public final Throwable error;

    ViewState(RepoDetail repoDetail, Throwable error) {
      this.repoDetail = repoDetail;
      this.error = error;
    }
  }
}
