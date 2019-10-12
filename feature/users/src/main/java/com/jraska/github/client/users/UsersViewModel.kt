package com.jraska.github.client.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jraska.github.client.Navigator
import com.jraska.github.client.Urls
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.core.android.rx.toLiveData
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.users.model.User
import com.jraska.github.client.users.model.UsersRepository
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

internal class UsersViewModel @Inject constructor(
  usersRepository: UsersRepository,
  appSchedulers: AppSchedulers,
  private val navigator: Navigator,
  private val eventAnalytics: EventAnalytics
) : ViewModel() {

  private val users: LiveData<ViewState>
  private val refreshSignal = PublishSubject.create<Any>()

  init {
    users = usersRepository.getUsers(0)
      .map { users -> ViewState.ShowUsers(users) as ViewState }
      .onErrorReturn { ViewState.Error(it) }
      .toObservable()
      .subscribeOn(appSchedulers.io)
      .startWith(ViewState.Loading)
      .repeatWhen { refreshSignal }
      .cache()
      .observeOn(appSchedulers.mainThread)
      .toLiveData()
  }

  fun users(): LiveData<ViewState> {
    return users
  }

  fun onRefresh() {
    refreshSignal.onNext(Unit)
  }

  fun onUserClicked(user: User) {
    val event = AnalyticsEvent.builder("open_user_detail")
      .addProperty("login", user.login)
      .build()

    eventAnalytics.report(event)

    navigator.startUserDetail(user.login)
  }

  fun onUserGitHubIconClicked(user: User) {
    val event = AnalyticsEvent.builder("open_github_from_list")
      .addProperty("login", user.login)
      .build()

    eventAnalytics.report(event)

    navigator.launchOnWeb(Urls.user(user.login))
  }

  fun onSettingsIconClicked() {
    eventAnalytics.report(AnalyticsEvent.create("open_settings_from_list"))

    navigator.showSettings()
  }

  fun onAboutIconClicked() {
    eventAnalytics.report(AnalyticsEvent.create("open_about_from_list"))

    navigator.showAbout()
  }

  sealed class ViewState {
    object Loading : ViewState()
    class Error(val error: Throwable) : ViewState()
    class ShowUsers(val users: List<User>) : ViewState()
  }
}
