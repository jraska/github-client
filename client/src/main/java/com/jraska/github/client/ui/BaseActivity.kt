package com.jraska.github.client.ui

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

abstract class BaseActivity : AppCompatActivity() {

  override fun setContentView(layoutResID: Int) {
    super.setContentView(layoutResID)
    onSetContentView()
  }

  override fun setContentView(view: View) {
    super.setContentView(view)
    onSetContentView()
  }

  override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
    super.setContentView(view, params)
    onSetContentView()
  }

  protected fun onSetContentView() {
    setSupportActionBar(toolbar)
  }
}
