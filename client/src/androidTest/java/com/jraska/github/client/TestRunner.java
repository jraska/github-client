package com.jraska.github.client;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

@SuppressWarnings("unused") // build.gradle
public final class TestRunner extends AndroidJUnitRunner {
  @Override
  public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    return super.newApplication(cl, TestUITestApp.class.getName(), context);
  }
}
