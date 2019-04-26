package com.jraska.github.client.users

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jraska.github.client.Navigator
import com.jraska.github.client.Urls
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.core.android.rx.RxLiveData
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.users.model.User
import com.jraska.github.client.users.model.UsersRepository
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

internal class UsersViewModel @Inject constructor(
  private val usersRepository: UsersRepository,
  private val appSchedulers: AppSchedulers,
  private val navigator: Navigator,
  private val eventAnalytics: EventAnalytics
) : ViewModel() {

  private val users: RxLiveData<ViewState>
  private var refreshingCache: OnSubscribeRefreshingCache<List<User>>? = null

  init {

    val viewStateObservable = usersInternal()
      .map { users -> ViewState.ShowUsers(users) as ViewState }
      .onErrorReturn { ViewState.Error(it) }
      .toObservable()
      .startWith(ViewState.Loading)

    users = RxLiveData.from(viewStateObservable)
  }

  fun users(): LiveData<ViewState> {
    return users
  }

  fun onRefresh() {
    refreshingCache!!.invalidate()
    users.resubscribe()
  }

  private fun usersInternal(): Single<List<User>> {
    val single = usersRepository.getUsers(0)
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.mainThread)

    refreshingCache = OnSubscribeRefreshingCache(single)
    return Single.create(refreshingCache!!)
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

  sealed class ViewState {
    object Loading : ViewState()
    class Error(val error: Throwable) : ViewState()
    class ShowUsers(val users: List<User>) : ViewState()
  }

  class OnSubscribeRefreshingCache<T>(private val source: Single<T>) : SingleOnSubscribe<T> {

    private val refresh = AtomicBoolean(true)
    @Volatile private var current: Single<T>? = null

    init {
      this.current = source
    }

    fun invalidate() {
      refresh.set(true)
    }

    @SuppressLint("CheckResult")
    @Throws(Exception::class)
    override fun subscribe(e: SingleEmitter<T>) {
      if (refresh.compareAndSet(true, false)) {
        current = source.cache()
      }
      current!!.subscribe({ e.onSuccess(it) }, { e.onError(it) })
    }
  }
}
