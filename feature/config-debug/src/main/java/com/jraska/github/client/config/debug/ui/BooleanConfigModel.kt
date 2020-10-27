package com.jraska.github.client.config.debug.ui

import android.view.View
import android.widget.Switch
import com.airbnb.epoxy.EpoxyModel
import com.jraska.github.client.config.debug.MutableConfigDef
import com.jraska.github.client.config.debug.R

internal class BooleanConfigModel(
  private val mutableConfigDef: MutableConfigDef
) : EpoxyModel<View>() {
  override fun getDefaultLayout() = R.layout.item_row_boolean_config

  override fun bind(view: View) {
    val switch = view as Switch
    switch.text = mutableConfigDef.name
  }
}
