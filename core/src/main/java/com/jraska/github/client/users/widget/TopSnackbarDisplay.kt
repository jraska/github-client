package com.jraska.github.client.users.widget

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.jraska.github.client.core.android.TopActivityProvider
import com.jraska.github.client.ui.SnackbarData
import com.jraska.github.client.ui.SnackbarDisplay
import javax.inject.Inject

class TopSnackbarDisplay @Inject constructor(
  private val topActivityProvider: TopActivityProvider
) : SnackbarDisplay {

  override fun showSnackbar(snackbarData: SnackbarData) {
    topActivityProvider.onTopActivity { showSnackbar(it, snackbarData) }
  }

  private fun showSnackbar(activity: Activity, snackbarData: SnackbarData) {
    val snackbarView = findSnackbarView(activity)

    val snackbar = Snackbar.make(snackbarView, snackbarData.text, snackbarData.length)
    snackbarData.action?.also { action -> snackbar.setAction(action.first) { action.second() } }

    snackbar.show()
  }

  private fun findSnackbarView(activity: Activity): View {
    val contentView = activity.findViewById<View>(android.R.id.content)
    return findCoordinatorLayout(contentView) ?: contentView
  }

  private fun findCoordinatorLayout(view: View): CoordinatorLayout? {
    if (view is CoordinatorLayout) {
      return view
    }

    if (view is ViewGroup) {
      val childCount = view.childCount
      for (childViewIndex in 0 until childCount) {
        val childView = view.getChildAt(childViewIndex)

        val coordinatorLayout = findCoordinatorLayout(childView)
        if (coordinatorLayout != null) {
          return coordinatorLayout
        }
      }
    }

    return null
  }

}
