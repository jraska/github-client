package com.jraska.github.client.push;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;

import javax.inject.Inject;

public final  class PushCallbacks implements Application.ActivityLifecycleCallbacks {
  private final PushIntentObserver intentObserver;

  public PushCallbacks(PushIntentObserver intentObserver) {
    this.intentObserver = intentObserver;
  }

  @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    if (activity instanceof LifecycleOwner) {
      ((LifecycleOwner) activity).getLifecycle().addObserver(intentObserver);
    }
  }

  @Override public void onActivityStarted(Activity activity) {
  }

  @Override public void onActivityResumed(Activity activity) {
  }

  @Override public void onActivityPaused(Activity activity) {
  }

  @Override public void onActivityStopped(Activity activity) {
  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
  }

  @Override public void onActivityDestroyed(Activity activity) {
  }
}
