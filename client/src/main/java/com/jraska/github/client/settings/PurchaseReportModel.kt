package com.jraska.github.client.settings

import android.view.View
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.jraska.github.client.R

internal class PurchaseReportModel(private val listener: PurchaseListener)
  : EpoxyModelWithHolder<PurchaseReportModel.PurchaseHolder>() {
  override fun getDefaultLayout(): Int {
    return R.layout.item_row_purchase
  }

  override fun createNewHolder(): PurchaseHolder {
    return PurchaseHolder()
  }

  override fun bind(holder: PurchaseHolder) {
    holder.submitButton.setOnClickListener({
      val valueEntered = holder.inputView.text.toString()
      listener.onPurchaseButtonClicked(valueEntered)
    })
  }

  class PurchaseHolder : EpoxyHolder() {
    @BindView(R.id.settings_purchase_input) lateinit var inputView: EditText
    @BindView(R.id.settings_purchase_submit_button) lateinit var submitButton: View

    override fun bindView(itemView: View) {
      ButterKnife.bind(this, itemView)
    }
  }

  internal interface PurchaseListener {
    fun onPurchaseButtonClicked(value: String)
  }
}
