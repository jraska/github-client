package com.jraska.github.client.core.android

import android.app.Activity
import androidx.annotation.AnyThread

interface TopActivityProvider {
  val topActivity: Activity?

  @AnyThread
  fun onTopActivity(action: (Activity) -> Unit)
}
