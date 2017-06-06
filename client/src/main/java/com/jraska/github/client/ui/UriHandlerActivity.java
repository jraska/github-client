package com.jraska.github.client.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.jraska.github.client.UriHandlerViewModel;

public class UriHandlerActivity extends BaseActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    UriHandlerViewModel viewModel = viewModel(UriHandlerViewModel.class);
    viewModel.handleDeepLink(getIntent().getData().toString());
    finish();
  }
}
