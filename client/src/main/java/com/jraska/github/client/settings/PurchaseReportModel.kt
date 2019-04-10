package com.jraska.github.client.settings

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.jraska.github.client.R
import kotlinx.android.synthetic.main.item_row_purchase.view.*

internal class PurchaseReportModel(private val listener: PurchaseListener)
  : EpoxyModel<View>() {
  override fun getDefaultLayout(): Int {
    return R.layout.item_row_purchase
  }

  override fun bind(itemView: View) {
    itemView.settings_purchase_submit_button.setOnClickListener {
      val valueEntered = itemView.settings_purchase_input.text.toString()
      listener.onPurchaseButtonClicked(valueEntered)
    }
  }

  internal interface PurchaseListener {
    fun onPurchaseButtonClicked(value: String)
  }
}
