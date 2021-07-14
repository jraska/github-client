package com.jraska.github.client

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import timber.log.Timber
import java.util.Date

internal class FirebaseConfigProxy(private val config: FirebaseRemoteConfig) : Config {
  private val onFetchCompleteListener: OnCompleteListener<Void> = OnCompleteListener {
    config.activate()
    Timber.d(
      "Config fetch complete. last fetch: %s",
      Date(config.info.fetchTimeMillis)
    )
    configsToCrashlytics()
  }

  override fun getBoolean(key: Config.Key): Boolean {
    return config.getBoolean(key.name)
  }

  override fun getLong(key: Config.Key): Long {
    return config.getLong(key.name)
  }

  override fun getString(key: Config.Key): String {
    return config.getString(key.name)
  }

  override fun triggerRefresh() {
    config.fetch(IGNORE_CACHE).addOnCompleteListener(onFetchCompleteListener)
  }

  fun fetch(): FirebaseConfigProxy {
    config.fetch(TWELVE_HOURS).addOnCompleteListener(onFetchCompleteListener)
    return this
  }

  fun setupDefaults(): FirebaseConfigProxy {
    config.setDefaultsAsync(R.xml.config_defaults)
    return this
  }

  private fun configsToCrashlytics() {
    config.getKeysByPrefix("")
      .map { it to config.getValue(it).asString() }
      .also { Timber.v("Setting Crashlytics properties from config: %s", it) }
      .forEach {
        FirebaseCrashlytics.getInstance().setCustomKey(it.first, it.second)
      }
  }

  companion object {
    private const val TWELVE_HOURS = (12 * 60 * 60).toLong()
    private const val IGNORE_CACHE: Long = 0
  }
}
