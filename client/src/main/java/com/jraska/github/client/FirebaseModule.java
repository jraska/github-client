package com.jraska.github.client;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.jraska.github.client.analytics.AnalyticsProperty;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.logging.CrashReporter;
import com.jraska.github.client.logging.FirebaseCrashReporter;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@SuppressLint("MissingPermission")
@Module
public class FirebaseModule {

  @Provides @PerApp FirebaseEventAnalytics firebaseAnalytics(Context context, Config config) {
    FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

    if (config.getBoolean("analytics_disabled")) {
      firebaseAnalytics.setAnalyticsCollectionEnabled(false);
      Timber.d("Analytics disabled");
    } else {
      firebaseAnalytics.setAnalyticsCollectionEnabled(true);
      Timber.d("Analytics enabled");
    }

    return new FirebaseEventAnalytics(firebaseAnalytics);
  }

  @Provides EventAnalytics eventAnalytics(FirebaseEventAnalytics analytics) {
    return analytics;
  }

  @Provides AnalyticsProperty analyticsProperty(FirebaseEventAnalytics analytics) {
    return analytics;
  }

  @Provides @PerApp CrashReporter firebaseCrash() {
    return new FirebaseCrashReporter();
  }

  @Provides @PerApp Config config() {
    FirebaseConfigProxy configProxy = new FirebaseConfigProxy(FirebaseRemoteConfig.getInstance());

    configProxy.setupDefaults().fetch();

    return configProxy;
  }

  @Provides @PerApp FirebasePerformance firebasePerformance(Config config) {
    FirebasePerformance performance = FirebasePerformance.getInstance();

    if (config.getBoolean("performance_collection_disabled")) {
      performance.setPerformanceCollectionEnabled(false);
      Timber.d("Performance collection disabled");
    } else {
      performance.setPerformanceCollectionEnabled(true);
      Timber.d("Performance collection enabled");
    }

    return performance;
  }

  @Provides @PerApp FirebaseDatabase firebaseDatabase() {
    return FirebaseDatabase.getInstance();
  }
}
