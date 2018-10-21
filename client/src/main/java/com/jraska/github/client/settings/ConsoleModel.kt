package com.jraska.github.client.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyModelWithView
import com.jraska.console.Console
import com.jraska.github.client.R

class ConsoleModel : EpoxyModelWithView<Console>() {
  override fun buildView(parent: ViewGroup): Console {
    return LayoutInflater.from(parent.context).inflate(R.layout.item_row_console, parent, false) as Console
  }
}
