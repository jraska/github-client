package com.jraska.github.client.config.debug.ui

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import com.jraska.github.client.config.debug.MutableConfigDef
import com.jraska.github.client.config.debug.R

internal class LongConfigModel(
  private val mutableConfigDef: MutableConfigDef
) : EpoxyModel<View>() {
  override fun getDefaultLayout() = R.layout.item_row_label_value_set_config

  override fun bind(view: View) {
    val labelText = view.findViewById<TextView>(R.id.item_row_boolean_config_label)
    labelText.text = mutableConfigDef.name

    val spinner = view.findViewById<Spinner>(R.id.item_row_boolean_config_spinner)
    spinner.prompt = mutableConfigDef.name
    spinner.adapter = ArrayAdapter(view.context, android.R.layout.simple_dropdown_item_1line, mutableConfigDef.domain)
  }
}
