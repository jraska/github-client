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
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class UsersViewModel extends ViewModel {

  private final UsersRepository usersRepository;
  private final AppSchedulers appSchedulers;
  private final Navigator navigator;
  private final EventAnalytics eventAnalytics;

  private final RxLiveData<ViewState> users;
  private OnSubscribeRefreshingCache<List<User>> refreshingCache;

  UsersViewModel(UsersRepository usersRepository, AppSchedulers appSchedulers,

                 Navigator navigator, EventAnalytics eventAnalytics) {
    this.usersRepository = usersRepository;
    this.appSchedulers = appSchedulers;
    this.navigator = navigator;
    this.eventAnalytics = eventAnalytics;

    Observable<ViewState> viewStateObservable = usersInternal()
      .map((data) -> new ViewState(null, data))
      .onErrorReturn((error) -> new ViewState(error, null))
      .toObservable()
      .startWith(new ViewState(null, null));

    users = RxLiveData.from(viewStateObservable);
  }

  public LiveData<ViewState> users() {
    return users;
  }

  public void onRefresh() {
    refreshingCache.invalidate();
    users.resubscribe();
  }

  private Single<List<User>> usersInternal() {
    Single<List<User>> single = usersRepository.getUsers(0)
      .subscribeOn(appSchedulers.io())
      .observeOn(appSchedulers.mainThread());

    refreshingCache = new OnSubscribeRefreshingCache<>(single);
    return Single.create(refreshingCache);
  }

  public void onUserClicked(User user) {
    AnalyticsEvent event = AnalyticsEvent.builder("open_user_detail")
      .addProperty("login", user.getLogin())
      .build();

    eventAnalytics.report(event);

    navigator.startUserDetail(user.getLogin());
  }

  public void onUserGitHubIconClicked(User user) {
    AnalyticsEvent event = AnalyticsEvent.builder("open_github_from_list")
      .addProperty("login", user.getLogin())
      .build();

    eventAnalytics.report(event);

    navigator.launchOnWeb(Urls.Companion.user(user.getLogin()));
  }

  public static final class ViewState {
    private final Throwable error;
    private final List<User> result;

    public ViewState(Throwable error, List<User> result) {
      this.error = error;
      this.result = result;
    }

    public boolean isLoading() {
      return result == null && error == null;
    }

    public Throwable error() {
      return error;
    }

    public List<User> result() {
      return result;
    }
  }

  public static class OnSubscribeRefreshingCache<T> implements SingleOnSubscribe<T> {

    private final AtomicBoolean refresh = new AtomicBoolean(true);
    private final Single<T> source;
    private volatile Single<T> current;

    public OnSubscribeRefreshingCache(Single<T> source) {
      this.source = source;
      this.current = source;
    }

    public void invalidate() {
      refresh.set(true);
    }

    @Override public void subscribe(SingleEmitter<T> e) throws Exception {
      if (refresh.compareAndSet(true, false)) {
        current = source.cache();
      }
      current.subscribe(e::onSuccess, e::onError);
    }
  }
}
