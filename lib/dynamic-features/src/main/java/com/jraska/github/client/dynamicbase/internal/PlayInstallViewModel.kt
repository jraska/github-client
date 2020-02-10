package com.jraska.github.client.dynamicbase.internal

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.jraska.github.client.common.lazyMap
import javax.inject.Inject
import timber.log.Timber

internal class PlayInstallViewModel @Inject constructor(
  private val splitInstallManager: SplitInstallManager,
  private val featureInstaller: PlayDynamicFeatureInstaller
) : ViewModel() {
  private val liveDataMap = lazyMap(this::startInstalling)
  private val listeners = mutableListOf<SplitInstallStateUpdatedListener>()
  private val confirmationDialog = MutableLiveData<ConfirmationDialogRequest>()

  fun moduleInstallation(moduleName: String): LiveData<ViewState> {
    return liveDataMap.getValue(moduleName)
  }

  fun confirmationDialog(): LiveData<ConfirmationDialogRequest> {
    return confirmationDialog
  }

  private fun startInstalling(moduleName: String): MutableLiveData<ViewState> {
    val liveData = MutableLiveData<ViewState>()
    liveData.value = ViewState.Pending(moduleName)

    splitInstallManager.beginInstallation(moduleName)

    val listener = Listener(moduleName, this)
    splitInstallManager.registerListener(listener)

    return liveData
  }

  override fun onCleared() {
    listeners.forEach { splitInstallManager.unregisterListener(it) }
    super.onCleared()
  }

  private fun onModuleStateUpdate(it: SplitInstallSessionState, moduleName: String) {
    Timber.d(it.toString())

    when (it.status()) {
      SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> onConfirmationRequired(it)
      SplitInstallSessionStatus.DOWNLOADED -> Unit
      SplitInstallSessionStatus.DOWNLOADING -> downloading(moduleName, it)
      SplitInstallSessionStatus.UNKNOWN -> Unit
      SplitInstallSessionStatus.PENDING -> pending(moduleName)
      SplitInstallSessionStatus.INSTALLING -> installing(moduleName)
      SplitInstallSessionStatus.INSTALLED -> publishSuccess(moduleName)
      SplitInstallSessionStatus.FAILED -> publishError(moduleName)
      SplitInstallSessionStatus.CANCELING -> publishError(moduleName)
      SplitInstallSessionStatus.CANCELED -> publishError(moduleName)
      else -> publishError(moduleName)
    }
  }

  private fun installing(moduleName: String) {
    liveDataMap.getValue(moduleName).value = ViewState.Installing(moduleName)
  }

  private fun pending(moduleName: String) {
    liveDataMap.getValue(moduleName).value = ViewState.Pending(moduleName)
  }

  private fun onConfirmationRequired(state: SplitInstallSessionState) {
    confirmationDialog.value = ConfirmationDialogRequest(state, splitInstallManager)
  }

  private fun downloading(moduleName: String, it: SplitInstallSessionState) {
    liveDataMap.getValue(moduleName).value = ViewState.Downloading(moduleName, it.bytesDownloaded(), it.totalBytesToDownload())
  }

  private fun publishError(moduleName: String) {
    val error = RuntimeException("Error installing feature")
    liveDataMap.getValue(moduleName).value = ViewState.Error(moduleName, error)
    featureInstaller.onFeatureInstallError(moduleName, error)
  }

  private fun publishSuccess(moduleName: String) {
    liveDataMap.getValue(moduleName).postValue(ViewState.Finish)
    featureInstaller.onFeatureInstalled(moduleName)
  }

  fun onConfirmationRequestCanceled(moduleName: String) {
    publishError(moduleName)
  }

  fun onConfirmationRequestSuccess(moduleName: String) {
    // We need to wait only for the listener
  }

  sealed class ViewState {
    class Pending(val moduleName: String) : ViewState()
    class Downloading(val moduleName: String, val fraction: Long, val total: Long) : ViewState()
    class Installing(val moduleName: String) : ViewState()
    object Finish : ViewState()
    class Error(val moduleName: String, val error: Throwable) : ViewState()
  }

  class Listener(val moduleName: String, val viewModel: PlayInstallViewModel) : SplitInstallStateUpdatedListener {
    override fun onStateUpdate(it: SplitInstallSessionState) {
      if (!it.moduleNames().contains(moduleName)) {
        return
      }

      viewModel.onModuleStateUpdate(it, moduleName)
    }
  }

  class ConfirmationDialogRequest(
    private val state: SplitInstallSessionState,
    private val splitInstallManager: SplitInstallManager
  ) {
    var consumed = false

    fun consume(activity: Activity, requestCode: Int) {
      consumed = true
      splitInstallManager.startConfirmationDialogForResult(state, activity, requestCode)
    }
  }
}
