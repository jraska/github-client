package com.jraska.github.client;

import android.content.Context;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.logging.CrashReporter;
import com.jraska.github.client.logging.FirebaseCrashReporter;
import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public class FirebaseModule {

  @Provides @PerApp EventAnalytics eventAnalytics(Context context, Config config) {
    FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

    if (config.getBoolean("analytics_disabled")) {
      firebaseAnalytics.setAnalyticsCollectionEnabled(false);
      Timber.d("Analytics disabled");
      return EventAnalytics.EMPTY;
    } else {
      firebaseAnalytics.setAnalyticsCollectionEnabled(true);
      Timber.d("Analytics enabled");
      return new FirebaseEventAnalytics(firebaseAnalytics);
    }
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
}
