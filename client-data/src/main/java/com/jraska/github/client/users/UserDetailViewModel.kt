package com.jraska.github.client.users

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.jraska.github.client.Config
import com.jraska.github.client.Navigator
import com.jraska.github.client.Urls
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.rx.RxLiveData
import java.util.*

class UserDetailViewModel internal constructor(private val usersRepository: UsersRepository,
                                               private val schedulers: AppSchedulers,
                                               private val navigator: Navigator,
                                               private val eventAnalytics: EventAnalytics,
                                               private val config: Config) : ViewModel() {

    private val liveDataMapping = HashMap<String, RxLiveData<ViewState>>()

    fun userDetail(login: String): LiveData<ViewState> {
        var liveData: RxLiveData<ViewState>? = liveDataMapping[login]
        if (liveData == null) {
            liveData = newUserLiveData(login)
            liveDataMapping[login] = liveData
        }

        return liveData
    }

    private fun newUserLiveData(login: String): RxLiveData<ViewState> {
        var reposInSection = config.getLong("user_detail_section_size").toInt()
        if (reposInSection <= 0) {
            reposInSection = 5
        }

        val viewStateObservable = usersRepository.getUserDetail(login, reposInSection)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .map { userDetail -> ViewState(null, userDetail) }
                .onErrorReturn { error -> ViewState(error, null) }
                .startWith(ViewState(null, null))

        return RxLiveData.from(viewStateObservable)
    }

    fun onUserGitHubIconClick(login: String) {
        val event = AnalyticsEvent.builder("open_github_from_detail")
                .addProperty("login", login)
                .build()

        eventAnalytics.report(event)

        navigator.launchOnWeb(Urls.user(login))
    }

    fun onRepoClicked(header: RepoHeader) {
        val event = AnalyticsEvent.builder("open_repo_from_detail")
                .addProperty("owner", header.owner)
                .addProperty("name", header.name)
                .build()

        eventAnalytics.report(event)

        navigator.startRepoDetail(header.fullName())
    }

    class ViewState internal constructor(private val error: Throwable?, private val result: UserDetail?) {

        val isLoading: Boolean
            get() = (result == null || result.basicStats == null) && error == null

        fun error(): Throwable? {
            return error
        }

        fun result(): UserDetail? {
            return result
        }
    }
}
