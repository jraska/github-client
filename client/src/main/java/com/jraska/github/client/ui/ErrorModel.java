package com.jraska.github.client.ui;

import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.epoxy.EpoxyModel;
import com.jraska.github.client.R;

public final class ErrorModel extends EpoxyModel<View> {
  @DrawableRes
  private final int icon;

  public ErrorModel(int icon) {
    this.icon = icon;
  }

  @Override protected int getDefaultLayout() {
    return R.layout.item_generic_error;
  }

  @Override public void bind(View view) {
    ((ImageView) view.findViewById(R.id.image)).setImageResource(icon);
  }

  static ErrorModel networkError() {
    return new ErrorModel(R.drawable.ic_signal_wifi_off_black_48dp);
  }

  static ErrorModel genericError() {
    return new ErrorModel(R.drawable.ic_error_outline_black_48dp);
  }
}
