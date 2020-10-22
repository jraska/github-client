package com.jraska.github.client.inappupdate

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory

class AppUpdateManagerFactoryProxy(private val context: Context) : UpdateManagerFactory {
  override fun create(): AppUpdateManager {
    return AppUpdateManagerFactory.create(context)
  }
}
