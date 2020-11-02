package com.jraska.github.client.settings

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel

internal class PurchaseReportModel(private val onPurchaseClicked: (String) -> Unit) : EpoxyModel<View>() {
  override fun getDefaultLayout(): Int {
    return R.layout.item_row_purchase
  }

  override fun bind(itemView: View) {
    itemView.findViewById<View>(R.id.settings_purchase_submit_button).setOnClickListener {
      val valueEntered = itemView.findViewById<TextView>(R.id.settings_purchase_input).text.toString()
      onPurchaseClicked(valueEntered)
    }
  }
}
