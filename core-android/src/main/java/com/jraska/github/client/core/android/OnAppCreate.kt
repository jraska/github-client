package com.jraska.github.client.core.android

import android.app.Application

interface OnAppCreate {
  fun onCreate(app: Application)

  /**
   * Higher priority will be executed first
   */
  fun priority(): Int = 0
}
