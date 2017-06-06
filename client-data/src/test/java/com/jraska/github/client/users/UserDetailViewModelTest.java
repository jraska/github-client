package com.jraska.github.client.users;

import com.jraska.github.client.Navigator;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.rx.AppSchedulers;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserDetailViewModelTest {
  @Test
  public void whenSameLoginMultipleTimes_thenOnlyOneObservableCreated() {
    UsersRepository usersRepository = mock(UsersRepository.class);

    // mock returning mock! Fairy dies! Too lazy now...
    Observable<UserDetail> detailObservable = Observable.just(mock(UserDetail.class));
    when(usersRepository.getUserDetail(any())).thenReturn(detailObservable);

    UserDetailViewModel viewModel = new UserDetailViewModel(usersRepository,
      trampoline(), mock(Navigator.class), EventAnalytics.EMPTY);

    String login = "someLogin";

    viewModel.userDetail(login);
    verify(usersRepository).getUserDetail(any());

    viewModel.userDetail(login);
    verify(usersRepository).getUserDetail(any());

    viewModel.userDetail("different" + login);
    verify(usersRepository, times(2)).getUserDetail(any());
  }

  public static AppSchedulers trampoline() {
    return new AppSchedulers(Schedulers.trampoline(), Schedulers.trampoline(),
      Schedulers.trampoline());
  }
}
