package com.jraska.github.client.ui

import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import java.net.UnknownHostException

object ErrorHandler {
  fun modelsForError(error: Throwable): List<EpoxyModel<*>> {
    if (error is UnknownHostException) {
      return listOf(ErrorModel.networkError())
    }

    return listOf(ErrorModel.genericError())
  }

  fun displayError(throwable: Throwable, toView: RecyclerView) {
    val models = modelsForError(throwable)

    val epoxyAdapter = SimpleEpoxyAdapter()
    epoxyAdapter.addModels(models)
    toView.adapter = epoxyAdapter
  }
}
