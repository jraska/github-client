package com.jraska.github.client.about

import android.view.View
import android.widget.TextView
import androidx.core.content.pm.PackageInfoCompat
import com.airbnb.epoxy.SimpleEpoxyModel
import java.time.Instant
import java.time.format.DateTimeFormatter.ISO_INSTANT

class VersionInfoModel : SimpleEpoxyModel(R.layout.about_item_version) {
  override fun bind(view: View) {
    super.bind(view)

    val context = view.context

    val buildTypeText = context.getString(R.string.about_version_build_type, BuildConfig.BUILD_TYPE)
    view.findViewById<TextView>(R.id.version_build_type).text = buildTypeText

    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionText = context.getString(
      R.string.about_version_name_and_code,
      packageInfo.versionName,
      PackageInfoCompat.getLongVersionCode(packageInfo)
    )
    view.findViewById<TextView>(R.id.version_name_and_code).text = versionText

    val firstInstallTimeText = ISO_INSTANT.format(
      Instant.ofEpochMilli(packageInfo.firstInstallTime)
    )
    val lastUpdateTimeText = ISO_INSTANT.format(
      Instant.ofEpochMilli(packageInfo.lastUpdateTime)
    )

    val updateInfoText = context.getString(
      R.string.about_version_install_info,
      lastUpdateTimeText,
      firstInstallTimeText
    )
    view.findViewById<TextView>(R.id.version_update_info).text = updateInfoText
  }

  override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
    return totalSpanCount
  }
}
