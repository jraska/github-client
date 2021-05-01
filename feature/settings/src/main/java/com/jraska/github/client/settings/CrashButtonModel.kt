package com.jraska.github.client.settings

import android.view.View
import android.widget.Button
import com.airbnb.epoxy.SimpleEpoxyModel

class CrashButtonModel(
  private val clickListener: () -> Unit,
) : SimpleEpoxyModel(R.layout.item_row_crash) {
  override fun bind(view: View) {
    super.bind(view)

    view.findViewById<Button>(R.id.settings_crash_button).setOnClickListener { clickListener() }
  }
}

