package com.jraska.github.client.ui;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.jraska.github.client.GitHubClientApp;
import com.jraska.github.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements LifecycleRegistryOwner {

  private final LifecycleRegistry registry = new LifecycleRegistry(this);

  @BindView(R.id.toolbar) Toolbar toolbar;

  @Override public LifecycleRegistry getLifecycle() {
    return registry;
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

  protected Fragment findFragmentById(int id) {
    return getSupportFragmentManager().findFragmentById(id);
  }

  protected GitHubClientApp app() {
    return (GitHubClientApp) getApplication();
  }

  protected ActivityComponent component() {
    return app().component().activityComponent(new ActivityModule(this));
  }
}
