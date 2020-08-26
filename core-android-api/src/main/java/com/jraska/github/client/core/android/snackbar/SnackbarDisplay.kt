package com.jraska.github.client.core.android.snackbar

import android.view.View

interface SnackbarDisplay {
  fun showSnackbar(snackbarData: SnackbarData)
}

class SnackbarData(
  val text: Int,
  val length: Int,
  val action: Pair<Int, View.OnClickListener?>? = null
)
