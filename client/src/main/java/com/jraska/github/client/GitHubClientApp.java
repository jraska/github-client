package com.jraska.github.client;

import android.app.Application;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.jraska.github.client.common.AppBuildConfig;
import com.jraska.github.client.http.DaggerHttpComponent;
import com.jraska.github.client.http.HttpComponent;
import com.jraska.github.client.http.HttpDependenciesModule;
import com.jraska.github.client.logging.ErrorReportTree;

import java.io.File;

import javax.inject.Inject;

import timber.log.Timber;

public class GitHubClientApp extends Application {
  private static final String CONFIG_ANALYTICS_DISABLED = "analytics_disabled";
  private static final String CONFIG_PERFORMANCE_COLLECTION_DISABLED = "performance_collection_disabled";

  private AppComponent appComponent;

  @Inject FirebaseAnalytics analytics;
  @Inject FirebasePerformance performance;
  @Inject ErrorReportTree errorReportTree;
  @Inject Config config;

  public AppComponent component() {
    return appComponent;
  }

  @AddTrace(name = "App.onCreate")
  @Override
  public void onCreate() {
    super.onCreate();

    appComponent = componentBuilder().build();
    appComponent.inject(this);

    Fresco.initialize(this);
    AndroidThreeTen.init(this);

    Timber.plant(errorReportTree);
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }

    setupAnalytics();
    setupPerformanceCollection();

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
    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "app_create");
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "app_create");
    analytics.logEvent("app_create", bundle);
  }

  private void setupPerformanceCollection() {
    if (config.getBoolean(CONFIG_PERFORMANCE_COLLECTION_DISABLED)) {
      performance.setPerformanceCollectionEnabled(false);
      Timber.d("Performance collection disabled");
    } else {
      performance.setPerformanceCollectionEnabled(true);
      Timber.d("Performance collection enabled");
    }
  }

  private void setupAnalytics() {
    if (config.getBoolean(CONFIG_ANALYTICS_DISABLED)) {
      analytics.setAnalyticsCollectionEnabled(false);
      Timber.d("Analytics disabled");
    } else {
      analytics.setAnalyticsCollectionEnabled(true);
      Timber.d("Analytics enabled");
    }
  }
}
