package com.jraska.github.client.users.model

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.common.lazyMap
import com.jraska.github.client.core.android.rx.toLiveData
import com.jraska.github.client.core.android.snackbar.SnackbarData
import com.jraska.github.client.core.android.snackbar.SnackbarDisplay
import com.jraska.github.client.navigation.Navigator
import com.jraska.github.client.navigation.Urls
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.users.R
import javax.inject.Inject

internal class RepoDetailViewModel @Inject constructor(
  private val usersRepository: UsersRepository,
  private val appSchedulers: AppSchedulers,
  private val navigator: Navigator,
  private val eventAnalytics: EventAnalytics,
  private val snackbarDisplay: SnackbarDisplay
) : ViewModel() {

  private val liveDataMap: Map<String, LiveData<ViewState>> = lazyMap(this::createRepoDetailLiveData)

  fun repoDetail(fullRepoName: String): LiveData<ViewState> {
    return liveDataMap.getValue(fullRepoName)
  }

  private fun createRepoDetailLiveData(fullRepoName: String): LiveData<ViewState> {
    val parts = fullRepoName.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    return usersRepository.getRepoDetail(parts[0], parts[1])
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.mainThread)
      .map { detail -> ViewState.ShowRepo(detail) as ViewState }
      .onErrorReturn { ViewState.Error(it) }
      .startWith(ViewState.Loading)
      .toLiveData()
  }

  fun onGitHubIconClicked(fullRepoName: String) {
    val event = AnalyticsEvent.builder("open_repo_from_detail")
      .addProperty("owner", RepoHeader.name(fullRepoName))
      .addProperty("name", RepoHeader.name(fullRepoName))
      .build()

    eventAnalytics.report(event)

    snackbarDisplay.showSnackbar(
      SnackbarData(
        R.string.repo_detail_open_web_text,
        Snackbar.LENGTH_INDEFINITE,
        R.string.repo_detail_open_web_action to View.OnClickListener {
          navigator.launchOnWeb(Urls.repo(fullRepoName))
        })
    )
  }

  sealed class ViewState {
    object Loading : ViewState()
    class Error(val error: Throwable) : ViewState()
    class ShowRepo(val repo: RepoDetail) : ViewState()
  }
}
