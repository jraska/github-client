package com.jraska.github.client.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jraska.github.client.Navigator
import com.jraska.github.client.Urls
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.rx.RxLiveData

class RepoDetailViewModel constructor(
  private val usersRepository: UsersRepository,
  private val appSchedulers: AppSchedulers,
  private val navigator: Navigator,
  private val eventAnalytics: EventAnalytics
) : ViewModel() {

  private val repoDetailLiveDataMap = HashMap<String, LiveData<ViewState>>()

  fun repoDetail(fullRepoName: String): LiveData<ViewState> {
    var liveData: LiveData<ViewState>? = repoDetailLiveDataMap[fullRepoName]
    if (liveData == null) {
      liveData = newRepoDetailLiveData(fullRepoName)
      repoDetailLiveDataMap[fullRepoName] = liveData
    }

    return liveData
  }

  private fun newRepoDetailLiveData(fullRepoName: String): LiveData<ViewState> {
    val parts = fullRepoName.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val stateObservable = usersRepository.getRepoDetail(parts[0], parts[1])
      .subscribeOn(appSchedulers.io())
      .observeOn(appSchedulers.mainThread())
      .map { detail -> ViewState(detail, null) }
      .onErrorReturn { throwable -> ViewState(null, throwable) }

    return RxLiveData.from(stateObservable)
  }

  fun onFitHubIconClicked(fullRepoName: String) {
    val event = AnalyticsEvent.builder("open_repo_from_detail")
      .addProperty("owner", RepoHeader.name(fullRepoName))
      .addProperty("name", RepoHeader.name(fullRepoName))
      .build()

    eventAnalytics.report(event)

    navigator.launchOnWeb(Urls.repo(fullRepoName))
  }

  class ViewState internal constructor(val repoDetail: RepoDetail?, val error: Throwable?)
}
