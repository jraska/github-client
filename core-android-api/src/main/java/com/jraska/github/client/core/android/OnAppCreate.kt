package com.jraska.github.client.core.android

import android.app.Application

interface OnAppCreate {
  fun onCreate(app: Application)
}

/**
 * Interface to parallelise startup of time consuming components.
 *
 * Initialisations which are not necessarily critical and don't have to block should happen here.
 */
interface OnAppCreateAsync {
  suspend fun onCreateAsync(app: Application)
}
