package com.jraska.github.client.about

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.viewModel

internal class AboutActivity : BaseActivity() {

  private val viewModel: AboutViewModel by lazy { viewModel(AboutViewModel::class.java) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel.viewState().observe(this, Observer { onNewState(it) })
  }

  private fun onNewState(state: AboutViewModel.ViewState) {
    when (state) {
      is AboutViewModel.ViewState.Data -> showData(state)
    }
  }

  private fun showData(state: AboutViewModel.ViewState.Data) {

  }

  companion object {
    fun start(inActivity: Activity) {
      val intent = Intent(inActivity, AboutActivity::class.java)
      inActivity.startActivity(intent)
    }
  }
}
