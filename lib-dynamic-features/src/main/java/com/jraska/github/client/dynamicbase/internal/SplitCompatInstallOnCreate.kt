package com.jraska.github.client.dynamicbase.internal

import android.app.Application
import com.google.android.play.core.splitcompat.SplitCompat
import com.jraska.github.client.core.android.OnAppCreate

internal class SplitCompatInstallOnCreate : OnAppCreate {
  override fun onCreate(app: Application) {
    // Emulates installation of future on demand modules using SplitCompat.
    SplitCompat.install(app)
  }
}
