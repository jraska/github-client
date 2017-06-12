package com.jraska.github.client.ui;

import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.SimpleEpoxyAdapter;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

public final class ErrorHandler {
  @SuppressWarnings("unchecked")
  List<EpoxyModel<?>> modelsForError(Throwable error) {
    if (error instanceof UnknownHostException) {
      return Collections.singletonList(ErrorModel.networkError());
    }

    return Collections.singletonList(ErrorModel.genericError());
  }

  static void displayError(Throwable throwable, RecyclerView toView) {
    ErrorHandler errorHandler = new ErrorHandler();
    List<EpoxyModel<?>> models = errorHandler.modelsForError(throwable);

    SimpleEpoxyAdapter epoxyAdapter = new SimpleEpoxyAdapter();
    epoxyAdapter.addModels(models);
    toView.setAdapter(epoxyAdapter);
  }
}
