package com.jraska.github.client

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

import javax.inject.Inject

class NotificationSetup @Inject constructor(
  private val notificationManager: NotificationManager,
  private val context: Context
) : OnAppCreate {

  override fun onCreate(app: Application) {
    setupChannels()
  }

  private fun setupChannels() {
    if (Build.VERSION.SDK_INT < 26) {
      return
    }

    val name = context.getString(R.string.channel_name_push)
    val description = context.getString(R.string.channel_description_push)
    val channel = NotificationChannel(PUSH_CHANNEL_ID, name,
      NotificationManager.IMPORTANCE_DEFAULT)
    channel.description = description

    notificationManager.createNotificationChannel(channel)
  }

  companion object {
    const val PUSH_CHANNEL_ID = "push_notifications"
  }
}
