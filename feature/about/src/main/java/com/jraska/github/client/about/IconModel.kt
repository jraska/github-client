package com.jraska.github.client.about

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.airbnb.epoxy.SimpleEpoxyModel

internal class IconModel(
  private val clickListener: () -> Unit,
  @DrawableRes val iconRes: Int,
  @StringRes val contentDescriptionRes: Int
) : SimpleEpoxyModel(R.layout.about_item_icon) {

  override fun bind(view: View) {
    super.bind(view)

    view.setOnClickListener { clickListener() }

    val aboutItemIcon = view.findViewById<ImageView>(R.id.about_item_icon)
    aboutItemIcon.setImageResource(iconRes)
    aboutItemIcon.contentDescription = view.context.getString(contentDescriptionRes)
  }

  override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
    return 1
  }
}
