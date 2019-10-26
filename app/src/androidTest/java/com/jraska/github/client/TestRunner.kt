package com.jraska.github.client

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.plugins.RxJavaPlugins

@Suppress("unused") // build.gradle
class TestRunner : AndroidJUnitRunner() {
  override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
    return super.newApplication(cl, TestUITestApp::class.java.name, context)
  }

  override fun onCreate(arguments: Bundle?) {
    super.onCreate(arguments)

    RxJavaPlugins.setInitComputationSchedulerHandler(
      Rx2Idler.create("RxJava 2.x Computation Scheduler")
    )
    RxJavaPlugins.setInitIoSchedulerHandler(
      Rx2Idler.create("RxJava 2.x IO Scheduler")
    )
  }
}
