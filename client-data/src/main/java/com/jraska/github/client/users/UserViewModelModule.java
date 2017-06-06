package com.jraska.github.client.users;

import android.arch.lifecycle.ViewModel;
import com.jraska.github.client.Navigator;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.rx.AppSchedulers;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class UserViewModelModule {
  @Provides
  @IntoMap
  @ClassKey(UsersViewModel.class)
  public static ViewModel provideUsersModel(UsersRepository repository, AppSchedulers schedulers,
                                            Navigator navigator, EventAnalytics analytics) {
    return new UsersViewModel(repository, schedulers, navigator, analytics);
  }

  @Provides
  @IntoMap
  @ClassKey(UserDetailViewModel.class)
  public static ViewModel provideDetailModel(UsersRepository repository, AppSchedulers schedulers,
                                             Navigator navigator, EventAnalytics analytics) {
    return new UserDetailViewModel(repository, schedulers, navigator, analytics);
  }
}
