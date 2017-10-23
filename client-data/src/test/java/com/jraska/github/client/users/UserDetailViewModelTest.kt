package com.jraska.github.client.users

import com.jraska.github.client.FakeConfig
import com.jraska.github.client.Navigator
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.rx.AppSchedulers
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.mockito.Mockito.*
import org.threeten.bp.LocalDateTime

class UserDetailViewModelTest {
  @Test
  fun whenSameLoginMultipleTimes_thenOnlyOneObservableCreated() {
    val usersRepository = mock(UsersRepository::class.java)


    val detailObservable = Observable.just(testDetail())
    `when`(usersRepository.getUserDetail("someLogin", 3)).thenReturn(detailObservable)
    `when`(usersRepository.getUserDetail("different", 3)).thenReturn(detailObservable)

    val config = FakeConfig.create("user_detail_section_size", 3L)

    val viewModel = UserDetailViewModel(usersRepository,
      trampoline(), mock(Navigator::class.java), EventAnalytics.EMPTY, config)

    val login = "someLogin"

    viewModel.userDetail(login)
    verify(usersRepository).getUserDetail("someLogin", 3)

    viewModel.userDetail(login)
    verify(usersRepository).getUserDetail("someLogin", 3)

    viewModel.userDetail("different")
    verify(usersRepository).getUserDetail("different", 3)
  }

  companion object {
    internal fun testDetail(): UserDetail {
      val user = User("login", "url", true, "url")
      val stats = UserStats(0, 0, 0, LocalDateTime.MIN)
      return UserDetail(user, stats, emptyList<RepoHeader>(), emptyList<RepoHeader>())
    }
  }
}

fun trampoline(): AppSchedulers {
  return AppSchedulers(Schedulers.trampoline(), Schedulers.trampoline(),
    Schedulers.trampoline())
}

