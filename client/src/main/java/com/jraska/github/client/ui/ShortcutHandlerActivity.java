package com.jraska.github.client.ui;

import android.os.Bundle;

import okhttp3.HttpUrl;

public class ShortcutHandlerActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ShortcutHandlerModel handlerModel = viewModel(ShortcutHandlerModel.class);

    HttpUrl httpUrl = HttpUrl.parse(getIntent().getData().toString());
    handlerModel.handleDeepLink(httpUrl);

    finish();
  }
}
