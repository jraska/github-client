package com.jraska.github.client.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jraska.github.client.DeepLinkHandler;

import javax.inject.Inject;

import okhttp3.HttpUrl;

public class UriHandlerActivity extends BaseActivity {
  @Inject DeepLinkHandler handler;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    component().inject(this);
    super.onCreate(savedInstanceState);

    HttpUrl deepLink = HttpUrl.parse(getIntent().getData().toString());
    handler.handleDeepLink(deepLink);
    finish();
  }
}
