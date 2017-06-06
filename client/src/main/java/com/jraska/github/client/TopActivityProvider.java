package com.jraska.github.client;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.jraska.github.client.ui.BaseActivity;

import javax.inject.Provider;

public class TopActivityProvider implements Provider<BaseActivity> {
  private BaseActivity topActivity;

  final Application.ActivityLifecycleCallbacks callbacks = new Application.ActivityLifecycleCallbacks() {
    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
      topActivity = (BaseActivity) activity;
    }

    @Override public void onActivityStarted(Activity activity) {
    }

    @Override public void onActivityResumed(Activity activity) {
      topActivity = (BaseActivity) activity;
    }

    @Override public void onActivityPaused(Activity activity) {
    }

    @Override public void onActivityStopped(Activity activity) {
    }

    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override public void onActivityDestroyed(Activity activity) {
    }
  };

  TopActivityProvider() {
  }

  @NonNull
  @Override
  public BaseActivity get() {
    if (topActivity == null) {
      throw new IllegalStateException("No activity");
    }

    return topActivity;
  }
}
