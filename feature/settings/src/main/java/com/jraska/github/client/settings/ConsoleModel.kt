package com.jraska.github.client.settings

import android.view.View
import com.airbnb.epoxy.EpoxyModel

internal class ConsoleModel(private val onConsoleClick: () -> Unit) : EpoxyModel<View>() {
  override fun getDefaultLayout() = R.layout.item_row_console

  override fun bind(view: View) {
    view.findViewById<View>(R.id.console_overlay).setOnClickListener { onConsoleClick() }
  }
}
