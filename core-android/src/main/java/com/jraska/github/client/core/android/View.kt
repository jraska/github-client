package com.jraska.github.client.core.android

import android.view.View

fun View.visible(visible: Boolean) {
  visibility = if (visible) {
    View.VISIBLE
  } else {
    View.GONE
  }
}
