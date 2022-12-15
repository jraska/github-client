package com.jraska.github.client

import android.content.Context
import javax.inject.Inject

interface AppVersion {
  fun appVersion(): String
}

@Suppress("DEPRECATION") // compatibility - since Android T
class AndroidAppVersion @Inject constructor(private val context: Context) : AppVersion {
  private val version by lazy {
    context.packageManager.getPackageInfo(context.packageName, 0).versionName
  }

  override fun appVersion(): String {
    return version
  }
}
