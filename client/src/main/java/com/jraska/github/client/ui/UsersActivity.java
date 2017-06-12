package com.jraska.github.client.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.SimpleEpoxyAdapter;
import com.jraska.github.client.R;
import com.jraska.github.client.common.Lists;
import com.jraska.github.client.users.User;
import com.jraska.github.client.users.UsersViewModel;

import java.util.List;

import butterknife.BindView;

public class UsersActivity extends BaseActivity implements UserModel.UserListener {
  private UsersViewModel usersViewModel;

  @BindView(R.id.users_refresh_swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.users_recycler) RecyclerView usersRecyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_users_list);

    usersViewModel = viewModel(UsersViewModel.class);

    usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    swipeRefreshLayout.setOnRefreshListener(usersViewModel::onRefresh);

    showProgressIndicator();

    usersViewModel.users().observe(this, this::setState);
  }

  @Override
  public void onUserClicked(User user) {
    usersViewModel.onUserClicked(user);
  }

  @Override
  public void onUserGitHubIconClicked(User user) {
    usersViewModel.onUserGitHubIconClicked(user);
  }

  void setState(UsersViewModel.ViewState state) {
    if (state.isLoading()) {
      showProgressIndicator();
    } else {
      hideProgressIndicator();
    }

    if (state.error() != null) {
      showError(state.error());
    } else if (state.result() != null) {
      setUsers(state.result());
    }
  }

  void setUsers(List<User> users) {
    SimpleEpoxyAdapter adapter = new SimpleEpoxyAdapter();
    List<UserModel> models = Lists.transform(users,
      user -> new UserModel(user, this));

    adapter.addModels(models);
    usersRecyclerView.setAdapter(adapter);
  }

  private void showError(Throwable error) {
    ErrorHandler.displayError(error, usersRecyclerView);
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
