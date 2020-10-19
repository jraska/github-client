package com.jraska.github.client.about

import android.view.View
import android.widget.TextView
import androidx.core.content.pm.PackageInfoCompat
import com.airbnb.epoxy.SimpleEpoxyModel

internal class VersionInfoModel : SimpleEpoxyModel(R.layout.about_item_version) {
  override fun bind(view: View) {
    super.bind(view)

    val context = view.context

    val buildTypeText = context.getString(R.string.about_version_build_type, BuildConfig.BUILD_TYPE)
    view.findViewById<TextView>(R.id.version_build_type).text = buildTypeText

    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionText = context.getString(R.string.about_version_name_and_code, packageInfo.versionName, PackageInfoCompat.getLongVersionCode(packageInfo))
    view.findViewById<TextView>(R.id.version_name_and_code).text = versionText
  }

  override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
    return totalSpanCount
  }
}
