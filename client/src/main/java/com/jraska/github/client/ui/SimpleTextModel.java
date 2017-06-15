package com.jraska.github.client.ui;

import android.widget.TextView;
import com.airbnb.epoxy.EpoxyModel;
import com.jraska.github.client.R;

final class SimpleTextModel extends EpoxyModel<TextView> {
  private final String text;

  public SimpleTextModel(String text) {
    this.text = text;
  }

  @Override protected int getDefaultLayout() {
    return R.layout.item_simple_text;
  }

  @Override public void bind(TextView textView) {
    textView.setText(text);
  }
}
