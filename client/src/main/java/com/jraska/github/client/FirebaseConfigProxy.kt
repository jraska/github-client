package com.jraska.github.client

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import timber.log.Timber
import java.util.*

internal class FirebaseConfigProxy(private val config: FirebaseRemoteConfig) : Config {
  private val onFetchCompleteListener: OnCompleteListener<Void> = OnCompleteListener {
    config.activateFetched()
    Timber.d("Config fetch complete. last fetch: %s",
      Date(config.info.fetchTimeMillis))
  }

  override fun getBoolean(key: String): Boolean {
    return config.getBoolean(key)
  }

  override fun getLong(key: String): Long {
    return config.getLong(key)
  }

  override fun getString(key: String): String {
    return config.getString(key)
  }

  override fun triggerRefresh() {
    config.fetch(IGNORE_CACHE).addOnCompleteListener(onFetchCompleteListener)
  }

  fun fetch(): FirebaseConfigProxy {
    config.fetch(TWELVE_HOURS).addOnCompleteListener(onFetchCompleteListener)
    return this
  }

  fun setupDefaults(): FirebaseConfigProxy {
    config.setDefaults(R.xml.config_defaults)
    return this
  }

  companion object {
    private const val TWELVE_HOURS = (12 * 60 * 60).toLong()
    private const val IGNORE_CACHE: Long = 1 // don't ever put zero! That is ignored and cache is used
  }
}
