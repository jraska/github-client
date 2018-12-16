package com.jraska.github.client.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.jraska.github.client.GitHubClientApp
import com.jraska.github.client.R

abstract class BaseActivity : AppCompatActivity() {

  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

  protected fun app(): GitHubClientApp {
    return application as GitHubClientApp
  }

  protected fun <T : ViewModel> viewModel(modelClass: Class<T>): T {
    val factory = app().viewModelFactory()
    return ViewModelProviders.of(this, factory).get(modelClass)
  }

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
