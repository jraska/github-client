package com.jraska.github.client.about.entrance

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.play.core.splitcompat.SplitCompat
import com.jraska.github.client.about.entrance.internal.PlayInstallViewModel
import com.jraska.github.client.about.entrance.internal.PlayInstallViewModel.ViewState
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.viewModel

internal class DynamicFeatureActivity : BaseActivity() {
  private val viewModel by lazy { viewModel(PlayInstallViewModel::class.java) }
  private val aboutModuleName by lazy { getString(R.string.title_dynamic_feature_about) }

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(newBase)
    SplitCompat.install(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel.moduleInstallation(moduleName())
      .observe(this, Observer { onNewState(it) })
  }

  private fun onNewState(viewState: ViewState) {
    when (viewState) {
      is ViewState.Loading -> {
      }
      is ViewState.Ready -> {
        startAboutActivityByClassName()
        finish()
      }
      is ViewState.Error -> {
        finish()
      }
    }
  }

  private fun moduleName() = intent.getStringExtra(KEY_MODULE_NAME)


  private fun startAboutActivityByClassName() {
    Intent().setClassName(packageName, "com.jraska.github.client.about.AboutActivity")
      .also {
        startActivity(it)
      }
  }

  companion object {
    private const val KEY_MODULE_NAME = "aiudha"
    fun start(inActivity: Activity, moduleName: String) {
      val intent = Intent(inActivity, DynamicFeatureActivity::class.java)
        .apply { putExtra(KEY_MODULE_NAME, moduleName) }
      inActivity.startActivity(intent)
    }
  }
}
