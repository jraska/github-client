package com.jraska.github.client.inappupdate

import com.google.android.play.core.appupdate.AppUpdateManager

interface UpdateManagerFactory {
  fun create(): AppUpdateManager
}
