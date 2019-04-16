package com.jraska.github.client.users.ui

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.airbnb.epoxy.EpoxyModel
import com.jraska.github.client.users.R

class ErrorModel(@DrawableRes private val icon: Int) : EpoxyModel<View>() {

  override fun getDefaultLayout(): Int {
    return R.layout.item_generic_error
  }

  override fun bind(view: View) {
    (view.findViewById<View>(R.id.image) as ImageView).setImageResource(icon)
  }

  companion object {

    internal fun networkError(): ErrorModel {
      return ErrorModel(R.drawable.ic_signal_wifi_off_black_48dp)
    }

    internal fun genericError(): ErrorModel {
      return ErrorModel(R.drawable.ic_error_outline_black_48dp)
    }
  }
}
