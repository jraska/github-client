package com.jraska.github.client.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.material.snackbar.Snackbar
import com.jraska.github.client.Owner
import com.jraska.github.client.WebLinkLauncher
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.common.lazyMap
import com.jraska.github.client.coroutines.AppDispatchers
import com.jraska.github.client.navigation.Urls
import com.jraska.github.client.repo.model.RepoDetail
import com.jraska.github.client.repo.model.RepoHeader
import com.jraska.github.client.repo.model.RepoRepository
import com.jraska.github.client.ui.SnackbarData
import com.jraska.github.client.ui.SnackbarDisplay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class RepoDetailViewModel @Inject constructor(
  private val repoRepository: RepoRepository,
  private val appDispatchers: AppDispatchers,
  private val webLinkLauncher: WebLinkLauncher,
  private val eventAnalytics: EventAnalytics,
  private val snackbarDisplay: SnackbarDisplay
) : ViewModel() {

  private val liveDataMap: Map<String, LiveData<ViewState>> = lazyMap(this::createRepoDetailLiveData)

  fun repoDetail(fullRepoName: String): LiveData<ViewState> {
    return liveDataMap.getValue(fullRepoName)
  }

  private fun createRepoDetailLiveData(fullRepoName: String): LiveData<ViewState> {
    val parts = fullRepoName.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    return repoRepository.getRepoDetail(parts[0], parts[1])
      .map { ViewState.ShowRepo(it) as ViewState }
      .onStart { emit(ViewState.Loading) }
      .catch { emit(ViewState.Error(it)) }
      .asLiveData(appDispatchers.io)
  }

  fun onGitHubIconClicked(fullRepoName: String) {
    val event = AnalyticsEvent.builder(ANALYTICS_OPEN_REPO)
      .addProperty("owner", RepoHeader.name(fullRepoName))
      .addProperty("name", RepoHeader.name(fullRepoName))
      .build()

    eventAnalytics.report(event)

    snackbarDisplay.showSnackbar(
      SnackbarData(
        R.string.repo_detail_open_web_text,
        Snackbar.LENGTH_INDEFINITE,
        R.string.repo_detail_open_web_action to { webLinkLauncher.launchOnWeb(Urls.repo(fullRepoName)) })
    )
  }

  sealed class ViewState {
    object Loading : ViewState()
    class Error(val error: Throwable) : ViewState()
    class ShowRepo(val repo: RepoDetail) : ViewState()
  }

  companion object {
    val ANALYTICS_OPEN_REPO = AnalyticsEvent.Key("open_repo_from_detail", Owner.USERS_TEAM)
  }
}
