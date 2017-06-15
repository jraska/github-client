package com.jraska.github.client.ui;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import com.airbnb.epoxy.SimpleEpoxyAdapter;
import com.jraska.github.client.R;
import com.jraska.github.client.users.RepoDetail;
import com.jraska.github.client.users.RepoDetailViewModel;

public class RepoDetailActivity extends BaseActivity {
  private static final String EXTRA_FULL_REPO_NAME = "fullRepoName";

  @BindView(R.id.repo_detail_recycler) RecyclerView recyclerView;

  private RepoDetailViewModel viewModel;

  private String fullRepoName() {
    return getIntent().getStringExtra(EXTRA_FULL_REPO_NAME);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_repo_detail);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    setTitle(fullRepoName());

    viewModel = viewModel(RepoDetailViewModel.class);
    LiveData<RepoDetailViewModel.ViewState> liveData = viewModel.repoDetail(fullRepoName());
    liveData.observe(this, this::setState);
  }

  void setState(RepoDetailViewModel.ViewState viewState) {
    if (viewState.repoDetail != null) {
      setRepoDetail(viewState.repoDetail);
    }

    if (viewState.error != null) {
      setError(viewState.error);
    }
  }

  void setError(Throwable error) {
    ErrorHandler.displayError(error, recyclerView);
  }

  void setRepoDetail(RepoDetail repoDetail) {
    SimpleEpoxyAdapter adapter = new SimpleEpoxyAdapter();
    adapter.addModels(new RepoDetailHeaderModel(repoDetail));

    String languageText = getString(R.string.repo_detail_language_used_template,
      repoDetail.data.language);
    adapter.addModels(new SimpleTextModel(languageText));

    String issuesText = getString(R.string.repo_detail_issues_template,
      String.valueOf(repoDetail.data.issuesCount));
    adapter.addModels(new SimpleTextModel(issuesText));

    recyclerView.setAdapter(adapter);
  }

  public static void start(Activity from, String fullRepoName) {
    Intent intent = new Intent(from, RepoDetailActivity.class);
    intent.putExtra(EXTRA_FULL_REPO_NAME, fullRepoName);

    from.startActivity(intent);
  }
}
