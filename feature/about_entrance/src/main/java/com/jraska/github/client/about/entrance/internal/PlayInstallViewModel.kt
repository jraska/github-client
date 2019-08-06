package com.jraska.github.client.about.entrance.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jraska.github.client.about.entrance.DynamicFeatureInstaller
import com.jraska.github.client.common.lazyMap
import com.jraska.github.client.rx.AppSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

internal class PlayInstallViewModel @Inject constructor(
  private val installer: DynamicFeatureInstaller,
  private val appSchedulers: AppSchedulers
) : ViewModel() {
  private val liveDataMap = lazyMap(this::startInstalling)
  private val disposables = CompositeDisposable()

  fun moduleInstallation(moduleName: String): LiveData<ViewState> {
    return liveDataMap.getValue(moduleName)
  }

  private fun startInstalling(moduleName: String): LiveData<ViewState> {
    val liveData = MutableLiveData<ViewState>()
    liveData.value = ViewState.Loading

    val disposable = installer.install(moduleName)
      .subscribeOn(appSchedulers.computation)
      .observeOn(appSchedulers.mainThread)
      .subscribe({ liveData.value = ViewState.Ready(moduleName) },
        { liveData.value = ViewState.Error(moduleName, it) })

    disposables.add(disposable)

    return liveData
  }

  sealed class ViewState {
    object Loading : ViewState()
    class Ready(val moduleName: String) : ViewState()
    class Error(val moduleName: String, val error: Throwable) : ViewState()
  }
}
