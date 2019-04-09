package com.jraska.github.client.ui

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.jraska.github.client.R

abstract class BaseActivity : AppCompatActivity() {

  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

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
    ButterKnife.bind(this)
    setSupportActionBar(toolbar)
  }
}
