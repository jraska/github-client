package com.jraska.github.client;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnitRunner;

import com.jraska.github.client.ui.UsersActivity;

public final class TestRunner extends AndroidJUnitRunner {
  @Override
  public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    return super.newApplication(cl, TestUITestApp.class.getName(), context);
  }

  @Override public void onStart() {
    super.onStart();
    startFirstActivityToEnsureSomeStarts();
  }

  private void startFirstActivityToEnsureSomeStarts() {
    Context targetContext = InstrumentationRegistry.getTargetContext();
    Intent intent = new Intent(targetContext, UsersActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    targetContext.startActivity(intent);
  }
}
