package com.jraska.github.client.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jraska.github.client.analytics.EventAnalytics
import javax.inject.Inject

internal class AboutViewModel @Inject constructor(
  private val analytics: EventAnalytics
) : ViewModel() {

  private val liveData = MutableLiveData<ViewState>()

  fun viewState(): LiveData<ViewState> {
    return liveData
  }

  sealed class ViewState {
    class Data() : ViewState()
  }
}
