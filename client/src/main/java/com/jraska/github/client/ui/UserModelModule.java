package com.jraska.github.client.ui;

import android.arch.lifecycle.ViewModel;
import com.jraska.github.client.Navigator;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.rx.AppSchedulers;
import com.jraska.github.client.users.UsersRepository;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class UserModelModule {
  @Provides
  @IntoMap
  @ClassKey(UsersViewModel.class)
  public static ViewModel provideModel(UsersRepository repository, AppSchedulers schedulers,
                                       Navigator navigator, EventAnalytics analytics) {
    return new UsersViewModel(repository, schedulers, navigator, analytics);
  }
}
