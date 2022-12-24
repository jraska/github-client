package com.jraska.github.client.android.test

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import org.junit.rules.ExternalResource
import java.io.Closeable

class LateLaunchActivityRule : ExternalResource() {
    private var activityScenario: Closeable? = null

    fun <A : Activity> launch(activityClass: Class<A>) {
      activityScenario = ActivityScenario.launch(activityClass)
    }

    override fun after() {
      activityScenario?.close()
    }
  }
