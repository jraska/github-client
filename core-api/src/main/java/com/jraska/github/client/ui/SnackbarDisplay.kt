package com.jraska.github.client.ui


interface SnackbarDisplay {
  fun showSnackbar(snackbarData: SnackbarData)
}

class SnackbarData(
  val text: Int,
  val length: Int,
  val action: Pair<Int, () -> Any?>? = null
)
