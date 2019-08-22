package com.jraska.github.client.dynamicbase.internal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.play.core.splitcompat.SplitCompat
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.viewModel
import com.jraska.github.client.dynamicbase.internal.PlayInstallViewModel.ConfirmationDialogRequest
import com.jraska.github.client.dynamicbase.internal.PlayInstallViewModel.ViewState
import timber.log.Timber

private const val CONFIRMATION_REQUEST_CODE = 101

internal class FeatureInstallActivity : BaseActivity() {
  private val viewModel by lazy { viewModel(PlayInstallViewModel::class.java) }

  private fun moduleName() = intent.getStringExtra(KEY_MODULE_NAME)

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(newBase)
    SplitCompat.install(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel.moduleInstallation(moduleName()).observe(this, Observer { onNewState(it) })
    viewModel.confirmationDialog().observe(this, Observer { onConfirmationDialog(it) })
  }

  override fun onBackPressed() {
    // We will finish by ourselves for now
  }

  override fun finish() {
    overridePendingTransition(0, 0)
    super.finish()
  }

  private fun onNewState(viewState: ViewState) {
    when (viewState) {
      is ViewState.Loading -> {
      }
      is ViewState.Finish -> {
        finish()
      }
      is ViewState.Error -> {
        displayError(viewState.error)
        finish()
      }
    }
  }

  private fun onConfirmationDialog(dialogRequest: ConfirmationDialogRequest) {
    if (!dialogRequest.consumed) {
      dialogRequest.consume(this, CONFIRMATION_REQUEST_CODE)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
      CONFIRMATION_REQUEST_CODE -> if (resultCode == Activity.RESULT_CANCELED) {
        viewModel.onConfirmationRequestCanceled(moduleName())
      } else {
        viewModel.onConfirmationRequestSuccess(moduleName())
      }
      else -> super.onActivityResult(requestCode, resultCode, data)
    }
  }

  private fun displayError(error: Throwable) {
    val message = "Erros installing ${moduleName()} feature."
    Timber.e(error, message)
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }

  companion object {
    private const val KEY_MODULE_NAME = "moduleName"

    fun start(context: Context, moduleName: String) {
      val intent = Intent(context, FeatureInstallActivity::class.java)
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .apply { putExtra(KEY_MODULE_NAME, moduleName) }
      context.startActivity(intent)
    }
  }
}
