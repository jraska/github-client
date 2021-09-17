package com.jraska.github.client.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jraska.github.client.DeepLinkLauncher
import com.jraska.github.client.Owner
import com.jraska.github.client.WebLinkLauncher
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.coroutines.AppDispatchers
import com.jraska.github.client.navigation.Urls
import com.jraska.github.client.users.model.User
import com.jraska.github.client.users.model.UsersRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class UsersViewModel @Inject constructor(
  private val usersRepository: UsersRepository,
  private val appDispatchers: AppDispatchers,
  private val deepLinkLauncher: DeepLinkLauncher,
  private val webLinkLauncher: WebLinkLauncher,
  private val eventAnalytics: EventAnalytics
) : ViewModel() {

  private val users = MediatorLiveData<ViewState>()
  private lateinit var currentData: LiveData<ViewState>

  init {
    setupLiveData()
  }

  fun users(): LiveData<ViewState> {
    return users
  }

  fun onRefresh() {
    users.removeSource(currentData)

    setupLiveData()
  }

  fun onUserClicked(user: User) {
    val event = AnalyticsEvent.builder(ANALYTICS_OPEN_USER)
      .addProperty("login", user.login)
      .build()

    eventAnalytics.report(event)

    deepLinkLauncher.launch(Urls.user(user.login))
  }

  fun onUserGitHubIconClicked(user: User) {
    val event = AnalyticsEvent.builder(ANALYTICS_OPEN_GITHUB)
      .addProperty("login", user.login)
      .build()

    eventAnalytics.report(event)

    webLinkLauncher.launchOnWeb(Urls.user(user.login))
  }

  fun onSettingsIconClicked() {
    eventAnalytics.report(AnalyticsEvent.create(ANALYTICS_OPEN_SETTINGS))

    deepLinkLauncher.launch(Urls.settings())
  }

  fun onAboutIconClicked() {
    eventAnalytics.report(AnalyticsEvent.create(ANALYTICS_OPEN_ABOUT))

    deepLinkLauncher.launch(Urls.about())
  }

  private fun setupLiveData() {
    currentData = newUsersLiveData()
    users.addSource(currentData) { users.value = it }
  }

  private fun newUsersLiveData() = flow {
    this.emit(ViewState.Loading)
    try {
      val users = usersRepository.getUsers(0)
      this.emit(ViewState.ShowUsers(users))
    } catch (ex: Exception) {
      this.emit(ViewState.Error(ex))
    }
  }.asLiveData(appDispatchers.io)

  sealed class ViewState {
    object Loading : ViewState()
    class Error(val error: Throwable) : ViewState()
    class ShowUsers(val users: List<User>) : ViewState()
  }

  companion object {
    val ANALYTICS_OPEN_USER = AnalyticsEvent.Key("open_user_detail", Owner.USERS_TEAM)
    val ANALYTICS_OPEN_GITHUB = AnalyticsEvent.Key("open_github_from_list", Owner.USERS_TEAM)
    val ANALYTICS_OPEN_SETTINGS = AnalyticsEvent.Key("open_settings_from_list", Owner.USERS_TEAM)
    val ANALYTICS_OPEN_ABOUT = AnalyticsEvent.Key("open_about_from_list", Owner.USERS_TEAM)
  }
}
