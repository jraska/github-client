package com.jraska.github.client.about

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.airbnb.epoxy.SimpleEpoxyModel
import kotlinx.android.synthetic.main.about_item_icon.view.*

internal class IconModel(
  private val clickListener: () -> Unit,
  @DrawableRes val iconRes: Int,
  @StringRes val contentDescriptionRes: Int) : SimpleEpoxyModel(R.layout.about_item_icon) {

  override fun bind(view: View) {
    super.bind(view)

    view.setOnClickListener { clickListener() }

    view.about_item_icon.setImageResource(iconRes)
    view.about_item_icon.contentDescription = view.context.getString(contentDescriptionRes)
  }

  override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
    return 1
  }
}
