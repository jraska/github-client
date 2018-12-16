package com.jraska.github.client.ui

import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import java.net.UnknownHostException

class ErrorHandler {
  internal fun modelsForError(error: Throwable): List<EpoxyModel<*>> {
    if (error is UnknownHostException) {
      return listOf(ErrorModel.networkError())
    }

    return listOf(ErrorModel.genericError())
  }

  companion object {
    internal fun displayError(throwable: Throwable, toView: RecyclerView) {
      val errorHandler = ErrorHandler()
      val models = errorHandler.modelsForError(throwable)

      val epoxyAdapter = SimpleEpoxyAdapter()
      epoxyAdapter.addModels(models)
      toView.adapter = epoxyAdapter
    }
  }
}
