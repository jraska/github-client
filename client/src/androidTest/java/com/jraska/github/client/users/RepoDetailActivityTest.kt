package com.jraska.github.client.users

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.jraska.github.client.DeepLinkLaunchTest
import com.jraska.github.client.Navigator
import com.jraska.github.client.R
import com.jraska.github.client.ViewModelFactoryDecorator
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.rx.AppSchedulers
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class RepoDetailActivityTest {
  @Mock lateinit var navigatorMock: Navigator
  @Mock lateinit var repositoryMock: UsersRepository

  @Before
  fun before() {
    MockitoAnnotations.initMocks(this)

    `when`(repositoryMock.getRepoDetail("jraska", "Falcon")).thenReturn(Observable.empty())
  }

  @Test
  fun whenFabClicked_thenNavigatesToGitHub() {
    val detailViewModel = RepoDetailViewModel(repositoryMock,
      AppSchedulers(AndroidSchedulers.mainThread(),
        Schedulers.io(), Schedulers.computation()), navigatorMock, EventAnalytics.EMPTY)
    ViewModelFactoryDecorator.setToApp(RepoDetailViewModel::class.java, detailViewModel)

    val deepLink = "https://github.com/jraska/Falcon"
    DeepLinkLaunchTest.launchDeepLink(deepLink)

    onView(withId(R.id.repo_detail_github_fab)).perform(click())
    verify<Navigator>(navigatorMock).launchOnWeb(HttpUrl.get(deepLink))
  }

  @After
  fun after() {
    ViewModelFactoryDecorator.removeDecorations()
  }
}
