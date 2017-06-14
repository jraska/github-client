package com.jraska.github.client.users;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.jraska.github.client.rx.AppSchedulers;
import com.jraska.github.client.rx.RxLiveData;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class RepoDetailViewModel extends ViewModel {
  private final UsersRepository usersRepository;
  private final AppSchedulers appSchedulers;

  private final Map<String, LiveData<ViewState>> repoDetailLiveDataMap = new HashMap<>();

  RepoDetailViewModel(UsersRepository usersRepository, AppSchedulers appSchedulers) {
    this.usersRepository = usersRepository;
    this.appSchedulers = appSchedulers;
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

  public static class ViewState {
    public final RepoDetail repoDetail;
    public final Throwable error;

    ViewState(RepoDetail repoDetail, Throwable error) {
      this.repoDetail = repoDetail;
      this.error = error;
    }
  }
}
