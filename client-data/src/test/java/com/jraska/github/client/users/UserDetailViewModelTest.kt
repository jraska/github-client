package com.jraska.github.client.users

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.github.client.FakeConfig
import com.jraska.github.client.Navigator
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.livedata.test
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.threeten.bp.Instant

class UserDetailViewModelTest {
  @get:Rule val testRule = InstantTaskExecutorRule()

  lateinit var usersRepository: UsersRepository

  private lateinit var viewModel: UserDetailViewModel

  @Before
  fun before() {
    usersRepository = mock(UsersRepository::class.java)

    val detailObservable = Observable.just(testDetail())
    `when`(usersRepository.getUserDetail("someLogin", 3)).thenReturn(detailObservable)
    `when`(usersRepository.getUserDetail("different", 3)).thenReturn(detailObservable)
    val config = FakeConfig.create(mapOf("user_detail_section_size" to 3L))

    viewModel = UserDetailViewModel(usersRepository,
      trampoline(), mock(Navigator::class.java), EventAnalytics.EMPTY, config)
  }

  @Test
  fun whenLiveData_thenCorrectUser() {
    viewModel.userDetail("someLogin")
      .test()
      .assertHasValue()
      .assertValue { it.isLoading.not() }
      .assertValue { it.error() == null }
  }

  @Test
  fun whenSameLoginMultipleTimes_thenOnlyOneObservableCreated() {
    val login = "someLogin"

    viewModel.userDetail(login).test().dispose()
    verify(usersRepository).getUserDetail("someLogin", 3)

    viewModel.userDetail(login).test().dispose()
    verify(usersRepository).getUserDetail("someLogin", 3)

    viewModel.userDetail("different").test().dispose()
    verify(usersRepository).getUserDetail("different", 3)
  }

  companion object {
    internal fun testDetail(): UserDetail {
      val user = User("someLogin", "url", true)
      val stats = UserStats(0, 0, 0, Instant.MIN)
      return UserDetail(user, stats, emptyList(), emptyList())
    }
  }
}

fun trampoline(): AppSchedulers {
  return AppSchedulers(Schedulers.trampoline(), Schedulers.trampoline(),
    Schedulers.trampoline())
}

