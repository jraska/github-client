package com.jraska.github.client.config.debug.ui

import android.view.View
import android.widget.Switch
import com.airbnb.epoxy.EpoxyModel
import com.jraska.github.client.config.MutableConfigDef
import com.jraska.github.client.config.debug.MutableConfig
import com.jraska.github.client.config.debug.R

internal class BooleanConfigModel(
  private val mutableConfigDef: MutableConfigDef,
  private val mutableConfig: MutableConfig
) : EpoxyModel<View>() {
  override fun getDefaultLayout() = R.layout.item_row_boolean_config

  override fun bind(view: View) {
    val switch = view as Switch
    switch.text = mutableConfigDef.key.name
    switch.isChecked = mutableConfig.getBoolean(mutableConfigDef.key)

    switch.setOnCheckedChangeListener { _, isChecked -> mutableConfig.setBoolean(mutableConfigDef.key, isChecked) }
  }
}
