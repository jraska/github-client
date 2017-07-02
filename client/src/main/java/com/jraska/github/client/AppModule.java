package com.jraska.github.client;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.jraska.github.client.common.AppBuildConfig;
import com.jraska.github.client.logging.VerboseLogger;
import com.jraska.github.client.rx.AppSchedulers;
import com.jraska.github.client.time.DateTimeProvider;
import com.jraska.github.client.time.RealDateTimeProvider;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Module
public class AppModule {
  private final GitHubClientApp app;

  public AppModule(@NonNull GitHubClientApp app) {
    this.app = app;
  }

  @Provides Context provideContext() {
    return app;
  }

  @Provides VerboseLogger timberLogger() {
    return message -> Timber.tag("Network").v(message);
  }

  @Provides @Reusable AppBuildConfig provideConfig() {
    return new AppBuildConfig(BuildConfig.DEBUG);
  }

  @Provides @PerApp AppSchedulers schedulers() {
    return new AppSchedulers(AndroidSchedulers.mainThread(),
      Schedulers.io(), Schedulers.computation());
  }

  @Provides @PerApp TopActivityProvider topActivityProvider() {
    return new TopActivityProvider();
  }

  @Provides @PerApp LayoutInflater provideLayoutInflater(Context context) {
    return LayoutInflater.from(context);
  }

  @Provides ViewModelProvider.Factory provideViewModelFactory(ViewModelFactory factory) {
    return factory;
  }

  @Provides @PerApp DateTimeProvider dateTimeProvider() {
    return new RealDateTimeProvider();
  }
}
