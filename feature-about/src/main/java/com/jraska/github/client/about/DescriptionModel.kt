package com.jraska.github.client.about

import com.airbnb.epoxy.SimpleEpoxyModel

internal class DescriptionModel(onDescriptionClick: () -> Unit) : SimpleEpoxyModel(R.layout.about_item_header) {
  init {
    onClick { onDescriptionClick() }
  }

  override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
    return totalSpanCount
  }
}
