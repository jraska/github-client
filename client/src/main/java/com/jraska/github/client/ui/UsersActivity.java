package com.jraska.github.client.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.airbnb.epoxy.SimpleEpoxyAdapter;
import com.jraska.github.client.Navigator;
import com.jraska.github.client.R;
import com.jraska.github.client.Urls;
import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.common.Lists;
import com.jraska.github.client.users.User;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class UsersActivity extends BaseActivity implements UserModel.UserListener {
  @Inject Navigator navigator;
  @Inject EventAnalytics eventAnalytics;

  private UsersViewModel usersViewModel;

  @BindView(R.id.users_refresh_swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.users_recycler) RecyclerView usersRecyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    component().inject(this);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_users_list);

    usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    swipeRefreshLayout.setOnRefreshListener(this::refresh);

    showProgressIndicator();

    usersViewModel = ViewModelProviders.of(this).get(UsersViewModel.class);
    usersViewModel.users().observe(this, this::setUsers);
  }

  @Override
  public void onUserClicked(User user) {
    AnalyticsEvent event = AnalyticsEvent.builder("open_user_detail")
      .addProperty("login", user.login)
      .build();

    eventAnalytics.report(event);

    navigator.startUserDetail(user.login);
  }

  @Override
  public void onUserGitHubIconClicked(User user) {
    AnalyticsEvent event = AnalyticsEvent.builder("open_github_from_list")
      .addProperty("login", user.login)
      .build();

    eventAnalytics.report(event);

    navigator.launchOnWeb(Urls.user(user.login));
  }

  void refresh() {
    // TODO(josef):  
  }

  public void setUsers(List<User> users) {
    hideProgressIndicator();

    SimpleEpoxyAdapter adapter = new SimpleEpoxyAdapter();
    List<UserModel> models = Lists.transform(users,
      user -> new UserModel(user, this));

    adapter.addModels(models);
    usersRecyclerView.setAdapter(adapter);
  }

  private void showErrorMessage(Throwable error) {
    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
  }

  private void showProgressIndicator() {
    ensureProgressIndicatorVisible();

    swipeRefreshLayout.setRefreshing(true);
  }

  private void hideProgressIndicator() {
    swipeRefreshLayout.setRefreshing(false);
  }

  private void ensureProgressIndicatorVisible() {
    // Workaround for start progress called before onMeasure
    // Issue: https://code.google.com/p/android/issues/detail?id=77712
    if (swipeRefreshLayout.getMeasuredHeight() == 0) {
      int circleSize = getResources().getDimensionPixelSize(R.dimen.swipe_progress_circle_diameter);
      swipeRefreshLayout.setProgressViewOffset(false, 0, circleSize);
    }
  }

  public static void start(Activity inActivity) {
    Intent intent = new Intent(inActivity, UsersActivity.class);
    inActivity.startActivity(intent);
  }
}
