package com.jraska.github.client.ui;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.SimpleEpoxyAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.jraska.github.client.R;
import com.jraska.github.client.users.RepoHeader;
import com.jraska.github.client.users.UserDetail;
import com.jraska.github.client.users.UserDetailViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserDetailActivity extends BaseActivity implements RepoHeaderModel.RepoListener {
  static final String EXTRA_USER_LOGIN = "login";

  @BindView(R.id.user_detail_avatar) SimpleDraweeView avatarView;
  @BindView(R.id.user_detail_recycler) RecyclerView recyclerView;

  private UserDetailViewModel userDetailViewModel;
  private Trace loadTrace;

  public String login() {
    return getIntent().getStringExtra(EXTRA_USER_LOGIN);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    loadTrace = FirebasePerformance.getInstance().newTrace("user_detail_load");
    loadTrace.start();

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_detail);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setNestedScrollingEnabled(false);

    setTitle(login());

    userDetailViewModel = viewModel(UserDetailViewModel.class);

    LiveData<UserDetailViewModel.ViewState> detailLiveData = userDetailViewModel.userDetail(login());
    detailLiveData.observe(this, this::setState);
  }

  @OnClick(R.id.user_detail_github_fab) void gitHubFabClicked() {
    userDetailViewModel.onUserGitHubIconClick(login());
  }

  private void setState(UserDetailViewModel.ViewState viewState) {
    if (viewState.isLoading()) {
      // TODO(josef): Show some progress
    }

    if (viewState.result() != null) {
      setUser(viewState.result());
    }

    if (viewState.error() != null) {
      showError(viewState.error());
    }
  }

  void setUser(UserDetail userDetail) {
    loadTrace.incrementCounter("set_user_detail");
    avatarView.setImageURI(userDetail.getUser().getAvatarUrl());

    if (userDetail.getBasicStats() == null) {
      loadTrace.incrementCounter("set_user_detail_incomplete");
      return;
    }

    SimpleEpoxyAdapter adapter = new SimpleEpoxyAdapter();

    List<EpoxyModel<?>> models = new ArrayList<>();
    models.add(new UserHeaderModel(userDetail.getBasicStats()));

    if (!userDetail.getPopularRepos().isEmpty()) {
      models.add(new ReposSectionModel(getString(R.string.repos_popular), userDetail.getPopularRepos(), this));
    }

    if (!userDetail.getContributedRepos().isEmpty()) {
      models.add(new ReposSectionModel(getString(R.string.repos_contributed), userDetail.getContributedRepos(), this));
    }

    adapter.addModels(models);

    recyclerView.setAdapter(adapter);
    loadTrace.stop();
  }

  public void showError(Throwable error) {
    ErrorHandler.displayError(error, recyclerView);
  }

  @Override public void onRepoClicked(RepoHeader header) {
    userDetailViewModel.onRepoClicked(header);
  }

  public static void start(Activity inActivity, @NonNull String login) {
    Intent intent = new Intent(inActivity, UserDetailActivity.class);
    intent.putExtra(EXTRA_USER_LOGIN, login);

    inActivity.startActivity(intent);
  }
}
