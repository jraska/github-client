package com.jraska.github.client.config.debug.ui

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import com.jraska.github.client.config.MutableConfigDef
import com.jraska.github.client.config.debug.MutableConfig
import com.jraska.github.client.config.debug.R

internal class LongConfigModel(
  private val configDef: MutableConfigDef,
  private val config: MutableConfig
) : EpoxyModel<View>() {
  override fun getDefaultLayout() = R.layout.item_row_label_value_set_config

  override fun bind(view: View) {
    val labelText = view.findViewById<TextView>(R.id.item_row_boolean_config_label)
    labelText.text = configDef.key.name

    val spinner = view.findViewById<Spinner>(R.id.item_row_boolean_config_spinner)

    val initialValue = config.getLong(configDef.key)
    val valuesToSelect = configDef.domain.toMutableList().also { it.add(0, initialValue) }

    spinner.adapter = ArrayAdapter(view.context, android.R.layout.simple_dropdown_item_1line, valuesToSelect)
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        config.setLong(configDef.key, (valuesToSelect[position] as Number).toLong())
      }

      override fun onNothingSelected(parent: AdapterView<*>) = Unit
    }
  }
}
