package com.jraska.github.client.about.entrance

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.play.core.splitcompat.SplitCompat
import com.jraska.github.client.about.entrance.internal.PlayInstallViewModel
import com.jraska.github.client.about.entrance.internal.PlayInstallViewModel.ViewState
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.viewModel
import timber.log.Timber

internal class DynamicFeatureActivity : BaseActivity() {
  private val viewModel by lazy { viewModel(PlayInstallViewModel::class.java) }

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(newBase)
    SplitCompat.install(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel.moduleInstallation(moduleName())
      .observe(this, Observer { onNewState(it) })
  }

  override fun finish() {
    overridePendingTransition(0, 0)
    super.finish()
  }

  private fun moduleName() = intent.getStringExtra(KEY_MODULE_NAME)

  private fun intentToLaunch() = intent.getParcelableExtra<Intent>(KEY_LAUNCH_INTENT)

  private fun onNewState(viewState: ViewState) {
    when (viewState) {
      is ViewState.Loading -> {
      }
      is ViewState.Ready -> {
        startActivity(intentToLaunch())
        finish()
      }
      is ViewState.Error -> {
        displayError(viewState.error)
        finish()
      }
    }
  }

  private fun displayError(error: Throwable) {
    val message = "Erros installing ${moduleName()} feature."
    Timber.e(error, message)
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }

  companion object {
    private const val KEY_MODULE_NAME = "moduleName"
    private const val KEY_LAUNCH_INTENT = "launchIntent"
    fun start(inActivity: Activity, moduleName: String, launchIntent: Intent) {
      val intent = Intent(inActivity, DynamicFeatureActivity::class.java)
        .apply { putExtra(KEY_MODULE_NAME, moduleName) }
        .apply { putExtra(KEY_LAUNCH_INTENT, launchIntent) }
      inActivity.startActivity(intent)
    }
  }
}
