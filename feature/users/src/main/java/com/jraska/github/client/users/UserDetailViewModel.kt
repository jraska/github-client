package com.jraska.github.client.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jraska.github.client.Config
import com.jraska.github.client.DeepLinkLauncher
import com.jraska.github.client.Owner
import com.jraska.github.client.WebLinkLauncher
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.common.lazyMap
import com.jraska.github.client.coroutines.AppDispatchers
import com.jraska.github.client.navigation.Urls
import com.jraska.github.client.users.model.RepoHeader
import com.jraska.github.client.users.model.UserDetail
import com.jraska.github.client.users.model.UsersRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class UserDetailViewModel @Inject constructor(
  private val usersRepository: UsersRepository,
  private val dispatchers: AppDispatchers,
  private val deepLinkLauncher: DeepLinkLauncher,
  private val webLinkLauncher: WebLinkLauncher,
  private val eventAnalytics: EventAnalytics,
  private val config: Config
) : ViewModel() {

  private val liveData: Map<String, LiveData<ViewState>> = lazyMap(this::createUserLiveData)

  fun userDetail(login: String): LiveData<ViewState> {
    return liveData.getValue(login)
  }

  private fun createUserLiveData(login: String): LiveData<ViewState> {
    var reposInSection = config.getLong(USER_DETAIL_SECTION_SIZE_KEY).toInt()
    if (reposInSection <= 0) {
      reposInSection = 5
    }

    return usersRepository.getUserDetail(login, reposInSection)
      .map { userDetail -> ViewState.DisplayUser(userDetail) as ViewState }
      .onStart { emit(ViewState.Loading) }
      .catch { emit(ViewState.Error(it)) }
      .asLiveData(dispatchers.io)
  }

  fun onUserGitHubIconClick(login: String) {
    val event = AnalyticsEvent.builder(ANALYTICS_OPEN_GITHUB)
      .addProperty("login", login)
      .build()

    eventAnalytics.report(event)

    webLinkLauncher.launchOnWeb(Urls.user(login))
  }

  fun onRepoClicked(header: RepoHeader) {
    val event = AnalyticsEvent.builder(ANALYTICS_OPEN_REPO)
      .addProperty("owner", header.owner)
      .addProperty("name", header.name)
      .build()

    eventAnalytics.report(event)

    deepLinkLauncher.launch(Urls.repo(header.fullName()))
  }

  sealed class ViewState {
    object Loading : ViewState()
    class Error(val error: Throwable) : ViewState()
    class DisplayUser(val user: UserDetail) : ViewState()
  }

  companion object {
    val USER_DETAIL_SECTION_SIZE_KEY = Config.Key("user_detail_section_size", Owner.USERS_TEAM)
    val ANALYTICS_OPEN_GITHUB = AnalyticsEvent.Key("open_github_from_detail", Owner.USERS_TEAM)
    val ANALYTICS_OPEN_REPO = AnalyticsEvent.Key("open_repo_from_detail", Owner.USERS_TEAM)
  }
}
