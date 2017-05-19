package com.jraska.github.client.users;

import com.jraska.github.client.Urls;
import com.jraska.github.client.Navigator;
import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.EventReporter;
import com.jraska.github.client.rx.AppSchedulers;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class UsersPresenter implements UsersViewEvents {
  private final UsersView view;
  private final UsersRepository usersRepository;
  private final AppSchedulers schedulers;
  private final Navigator navigator;
  private final EventReporter eventReporter;
  private Disposable subscription;

  public UsersPresenter(UsersView view, UsersRepository usersRepository,
                        AppSchedulers schedulers, Navigator navigator, EventReporter eventReporter) {
    this.view = view;
    this.usersRepository = usersRepository;
    this.schedulers = schedulers;
    this.navigator = navigator;
    this.eventReporter = eventReporter;
  }

  public void onCreate() {
    subscription = usersRepository.getUsers(0)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.mainThread())
        .doOnSubscribe(disposable -> view.startDisplayProgress())
        .doFinally(view::stopDisplayProgress)
        .subscribe(this::onLoaded, this::onLoadError);
  }

  public void onDestroy() {
    if (subscription != null) {
      subscription.dispose();
    }
  }

  void onLoaded(List<User> users) {
    view.setUsers(users);
  }

  void onLoadError(Throwable throwable) {
    view.showMessage(throwable.toString());
  }

  @Override public void onUserItemClick(User user) {
    AnalyticsEvent event = AnalyticsEvent.builder("open_user_detail")
        .addProperty("login", user.login)
        .build();

    eventReporter.report(event);

    navigator.startUserDetail(user.login);
  }

  @Override public void onUserGitHubIconClick(User user) {
    AnalyticsEvent event = AnalyticsEvent.builder("open_github_from_list")
        .addProperty("login", user.login)
        .build();

    eventReporter.report(event);

    navigator.launchOnWeb(Urls.user(user.login));
  }

  @Override public void onRefresh() {
    onCreate();
  }
}
