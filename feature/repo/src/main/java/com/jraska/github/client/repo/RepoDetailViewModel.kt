package com.jraska.github.client.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.google.android.material.snackbar.Snackbar
import com.jraska.github.client.Owner
import com.jraska.github.client.WebLinkLauncher
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.common.lazyMap
import com.jraska.github.client.ui.SnackbarData
import com.jraska.github.client.ui.SnackbarDisplay
import com.jraska.github.client.navigation.Urls
import com.jraska.github.client.repo.model.RepoDetail
import com.jraska.github.client.repo.model.RepoHeader
import com.jraska.github.client.repo.model.RepoRepository
import com.jraska.github.client.rx.AppSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class RepoDetailViewModel @Inject constructor(
  private val repoRepository: RepoRepository,
  private val appSchedulers: AppSchedulers,
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
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.mainThread)
      .map<ViewState> { detail -> ViewState.ShowRepo(detail) }
      .onErrorReturn { ViewState.Error(it) }
      .startWith(Single.just(ViewState.Loading))
      .toFlowable(BackpressureStrategy.MISSING)
      .toLiveData()
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
