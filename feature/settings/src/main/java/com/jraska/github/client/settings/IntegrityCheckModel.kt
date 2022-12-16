package com.jraska.github.client.settings

import android.view.View
import com.airbnb.epoxy.EpoxyModel

internal class IntegrityCheckModel(private val onIntegrityCheckClick: () -> Unit) :
  EpoxyModel<View>() {
  override fun getDefaultLayout(): Int {
    return R.layout.item_row_integrity
  }

  override fun bind(itemView: View) {
    itemView.findViewById<View>(R.id.settings_integrity_run_button).setOnClickListener {
      onIntegrityCheckClick()
    }
  }
}
