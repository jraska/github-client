package com.jraska.github.client.android

import com.jraska.github.client.ui.SnackbarData
import com.jraska.github.client.ui.SnackbarDisplay

class FakeSnackbarDisplay : SnackbarDisplay {
  private val snackbarsInvoked = mutableListOf<SnackbarData>()

  fun snackbarsInvoked(): List<SnackbarData> = snackbarsInvoked

  override fun showSnackbar(snackbarData: SnackbarData) {
    snackbarsInvoked.add(snackbarData)
  }
}
