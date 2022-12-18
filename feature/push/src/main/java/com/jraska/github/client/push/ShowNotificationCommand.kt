package com.jraska.github.client.push

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.jraska.github.client.push.PushExecuteResult.FAILURE
import javax.inject.Inject

internal class ShowNotificationCommand @Inject constructor(
  private val context: Context,
  private val notificationManager: NotificationManager
) : PushActionCommand {
  override fun execute(action: PushAction): PushExecuteResult {
    val title = action.parameters["title"] ?: return FAILURE
    val message = action.parameters["message"] ?: return FAILURE
    val deepLink = action.parameters["clickDeepLink"] ?: return FAILURE

    val uriActivityIntent = Intent(Intent.ACTION_VIEW)
    uriActivityIntent.`package` = context.packageName
    uriActivityIntent.data = Uri.parse(deepLink)

    val linkContentIntent =
      PendingIntent.getActivity(context, 0, uriActivityIntent, PendingIntent.FLAG_IMMUTABLE)

    val notification = NotificationCompat.Builder(context, NotificationSetup.PUSH_CHANNEL_ID)
      .setSmallIcon(android.R.drawable.ic_dialog_info)
      .setContentTitle(title)
      .setContentText(message)
      .setContentIntent(linkContentIntent)
      .setAutoCancel(true)
      .build()

    notificationManager.notify(1, notification)
    return PushExecuteResult.SUCCESS
  }
}
