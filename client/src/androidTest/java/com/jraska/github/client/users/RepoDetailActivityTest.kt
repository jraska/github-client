package com.jraska.github.client.users

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.jraska.github.client.*
import com.jraska.github.client.analytics.EventAnalytics
import io.reactivex.Observable
import okhttp3.HttpUrl
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class RepoDetailActivityTest {
  @Mock lateinit var navigatorMock: Navigator
  @Mock lateinit var repositoryMock: UsersRepository

  @get:Rule
  var passRule = MakeTestsPassRule()

  @Before
  fun before() {
    MockitoAnnotations.initMocks(this)

    `when`(repositoryMock.getRepoDetail("jraska", "Falcon")).thenReturn(Observable.empty())
  }

  @Test
  fun whenFabClicked_thenNavigatesToGitHub() {
    val detailViewModel = RepoDetailViewModel(repositoryMock,
      AppModule.schedulers(), navigatorMock, EventAnalytics.EMPTY)
    ViewModelFactoryDecorator.setToApp(RepoDetailViewModel::class.java, detailViewModel)

    val deepLink = "https://github.com/jraska/Falcon"
    DeepLinkLaunchTest.launchDeepLink(deepLink)

    onView(withId(R.id.repo_detail_github_fab)).perform(ViewActions.click())
    verify<Navigator>(navigatorMock).launchOnWeb(HttpUrl.parse(deepLink)!!)
  }
}
