package com.jraska.github.client.dynamicbase.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.jraska.github.client.common.lazyMap
import timber.log.Timber
import javax.inject.Inject

internal class PlayInstallViewModel @Inject constructor(
  private val splitInstallManager: SplitInstallManager,
  private val featureInstaller: PlayDynamicFeatureInstaller
) : ViewModel() {
  private val liveDataMap = lazyMap(this::startInstalling)
  private val listeners = mutableListOf<SplitInstallStateUpdatedListener>()

  fun moduleInstallation(moduleName: String): LiveData<ViewState> {
    return liveDataMap.getValue(moduleName)
  }

  private fun publishError(moduleName: String) {
    val error = RuntimeException("Error downloaded")
    liveDataMap.getValue(moduleName).value = ViewState.Error(error)
    featureInstaller.onFeatureInstallError(moduleName, error)
  }

  private fun publishSuccess(moduleName: String) {
    liveDataMap.getValue(moduleName).value = ViewState.Finish
    featureInstaller.onFeatureInstalled(moduleName)
  }

  private fun startInstalling(moduleName: String): MutableLiveData<ViewState> {
    val liveData = MutableLiveData<ViewState>()
    liveData.value = ViewState.Loading

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
      SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> onConfirmationRequired(moduleName)
      SplitInstallSessionStatus.DOWNLOADED -> Unit
      SplitInstallSessionStatus.DOWNLOADING -> Unit
      SplitInstallSessionStatus.UNKNOWN -> Unit
      SplitInstallSessionStatus.PENDING -> Unit
      SplitInstallSessionStatus.INSTALLING -> Unit
      SplitInstallSessionStatus.INSTALLED -> publishSuccess(moduleName)
      SplitInstallSessionStatus.FAILED -> publishError(moduleName)
      SplitInstallSessionStatus.CANCELING -> publishError(moduleName)
      SplitInstallSessionStatus.CANCELED -> publishError(moduleName)
      else -> publishError(moduleName)
    }
  }

  private fun onConfirmationRequired(moduleName: String) {
    /* This may occur when attempting to download a sufficiently large module.
    In order to see this, the application has to be uploaded to the Play Store.
    Then features can be requested until the confirmation path is triggered.*/
    //    splitInstallManager.startConfirmationDialogForResult(it, this, CONFIRMATION_REQUEST_CODE)
    throw NotImplementedError()
  }

  sealed class ViewState {
    object Loading : ViewState()
    object Finish : ViewState()
    class Error(val error: Throwable) : ViewState()
  }

  class Listener(val moduleName: String, val viewModel: PlayInstallViewModel) : SplitInstallStateUpdatedListener {
    override fun onStateUpdate(it: SplitInstallSessionState) {
      if (!it.moduleNames().contains(moduleName)) {
        return
      }

      viewModel.onModuleStateUpdate(it, moduleName)
    }
  }
}
