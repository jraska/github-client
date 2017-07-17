package com.jraska.github.client.users;

import com.jraska.github.client.FakeConfig;
import com.jraska.github.client.Navigator;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.rx.AppSchedulers;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;

import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserDetailViewModelTest {
  @Test
  public void whenSameLoginMultipleTimes_thenOnlyOneObservableCreated() {
    UsersRepository usersRepository = mock(UsersRepository.class);


    Observable<UserDetail> detailObservable = Observable.just(testDetail());
    when(usersRepository.getUserDetail(any(), anyInt())).thenReturn(detailObservable);

    FakeConfig config = FakeConfig.create("user_detail_section_size", 3L);

    UserDetailViewModel viewModel = new UserDetailViewModel(usersRepository,
      trampoline(), mock(Navigator.class), EventAnalytics.EMPTY, config);

    String login = "someLogin";

    viewModel.userDetail(login);
    verify(usersRepository).getUserDetail(any(), eq(3));

    viewModel.userDetail(login);
    verify(usersRepository).getUserDetail(any(), anyInt());

    viewModel.userDetail("different" + login);
    verify(usersRepository, times(2)).getUserDetail(any(), eq(3));
  }

  public static AppSchedulers trampoline() {
    return new AppSchedulers(Schedulers.trampoline(), Schedulers.trampoline(),
      Schedulers.trampoline());
  }

  static UserDetail testDetail() {
    User user = new User("login", "url", true, "url");
    UserStats stats = new UserStats(0, 0, 0, LocalDateTime.MIN);
    return new UserDetail(user, stats, Collections.emptyList(), Collections.emptyList());
  }
}
