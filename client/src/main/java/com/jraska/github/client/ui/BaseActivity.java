package com.jraska.github.client.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.jraska.github.client.GitHubClientApp;
import com.jraska.github.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;

  protected final GitHubClientApp app() {
    return (GitHubClientApp) getApplication();
  }

  protected final <T extends ViewModel> T viewModel(Class<T> modelClass) {
    ViewModelProvider.Factory factory = app().viewModelFactory();
    return ViewModelProviders.of(this, factory).get(modelClass);
  }

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    onSetContentView();
  }

  @Override
  public void setContentView(View view) {
    super.setContentView(view);
    onSetContentView();
  }

  @Override
  public void setContentView(View view, ViewGroup.LayoutParams params) {
    super.setContentView(view, params);
    onSetContentView();
  }

  protected void onSetContentView() {
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
  }
}
