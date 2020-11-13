package com.jraska.github.client.config.debug.ui

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.jraska.github.client.config.debug.R

internal class HeaderConfigModel : EpoxyModel<View>() {
  override fun getDefaultLayout() = R.layout.item_row_config_section_title

  override fun bind(view: View) = Unit
}
