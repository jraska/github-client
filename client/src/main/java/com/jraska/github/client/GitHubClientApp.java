package com.jraska.github.client;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.perf.metrics.AddTrace;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.common.AppBuildConfig;
import com.jraska.github.client.http.DaggerHttpComponent;
import com.jraska.github.client.http.HttpComponent;
import com.jraska.github.client.http.HttpDependenciesModule;
import com.jraska.github.client.logging.ErrorReportTree;

import java.io.File;

import javax.inject.Inject;

import timber.log.Timber;

public class GitHubClientApp extends Application {

  @Inject EventAnalytics eventAnalytics;
  @Inject ErrorReportTree errorReportTree;
  @Inject TopActivityProvider topActivityProvider;
  @Inject ViewModelProvider.Factory viewModelFactory;

  public ViewModelProvider.Factory viewModelFactory() {
    return viewModelFactory;
  }

  @AddTrace(name = "App.userDetail")
  @Override
  public void onCreate() {
    super.onCreate();

    AppComponent appComponent = componentBuilder().build();
    appComponent.inject(this);

    Fresco.initialize(this);
    AndroidThreeTen.init(this);

    Timber.plant(errorReportTree);
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }

    registerActivityLifecycleCallbacks(topActivityProvider.callbacks);
    logAppCreateEvent();
  }

  protected DaggerAppComponent.Builder componentBuilder() {
    return DaggerAppComponent.builder()
      .appModule(new AppModule(this))
      .httpComponent(httpComponent());
  }

  protected HttpComponent httpComponent() {
    HttpDependenciesModule dependenciesModule = new HttpDependenciesModule(
      new AppBuildConfig(BuildConfig.DEBUG), new File(getCacheDir(), "network"));

    return DaggerHttpComponent.builder()
      .httpDependenciesModule(dependenciesModule)
      .build();
  }

  private void logAppCreateEvent() {
    AnalyticsEvent createEvent = AnalyticsEvent.create("app_create");
    eventAnalytics.report(createEvent);
  }
}
