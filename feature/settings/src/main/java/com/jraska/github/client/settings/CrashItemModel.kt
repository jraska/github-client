package com.jraska.github.client.settings

import android.view.View
import com.airbnb.epoxy.SimpleEpoxyModel

class CrashItemModel(
  private val clickListener: () -> Unit,
) : SimpleEpoxyModel(R.layout.item_row_crash) {
  override fun bind(view: View) {
    super.bind(view)

    view.setOnClickListener { clickListener() }
  }
}

