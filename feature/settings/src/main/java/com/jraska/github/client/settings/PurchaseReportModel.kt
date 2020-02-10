package com.jraska.github.client.settings

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import kotlinx.android.synthetic.main.item_row_purchase.view.settings_purchase_input
import kotlinx.android.synthetic.main.item_row_purchase.view.settings_purchase_submit_button

internal class PurchaseReportModel(private val onPurchaseClicked: (String) -> Unit) :
  EpoxyModel<View>() {
  override fun getDefaultLayout(): Int {
    return R.layout.item_row_purchase
  }

  override fun bind(itemView: View) {
    itemView.settings_purchase_submit_button.setOnClickListener {
      val valueEntered = itemView.settings_purchase_input.text.toString()
      onPurchaseClicked(valueEntered)
    }
  }
}
