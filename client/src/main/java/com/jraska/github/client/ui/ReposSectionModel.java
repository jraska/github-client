package com.jraska.github.client.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.airbnb.epoxy.SimpleEpoxyAdapter;
import com.jraska.github.client.R;
import com.jraska.github.client.common.Lists;
import com.jraska.github.client.users.RepoHeader;
import com.jraska.github.client.widget.RepeaterLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ReposSectionModel extends EpoxyModelWithHolder<ReposSectionModel.ReposHolder> {
  private final String title;
  private final List<RepoHeader> repos;
  private final RepoHeaderModel.RepoListener repoListener;

  public ReposSectionModel(String title, List<RepoHeader> repos,
                           RepoHeaderModel.RepoListener repoListener) {
    this.title = title;
    this.repos = repos;
    this.repoListener = repoListener;
  }

  @Override protected ReposHolder createNewHolder() {
    return new ReposHolder();
  }

  @Override protected int getDefaultLayout() {
    return R.layout.item_repos_section;
  }

  @Override public void bind(ReposHolder holder) {
    holder.reposTitle.setText(title);

    SimpleEpoxyAdapter adapter = new SimpleEpoxyAdapter();
    adapter.addModels(Lists.transform(repos, repo -> new RepoHeaderModel(repo, repoListener)));

    holder.reposRepeater.setAdapter(adapter);
  }

  static class ReposHolder extends EpoxyHolder {
    @BindView(R.id.repos_repeater) RepeaterLayout reposRepeater;
    @BindView(R.id.repos_container) ViewGroup reposContainer;
    @BindView(R.id.repos_title) TextView reposTitle;

    @Override protected void bindView(View itemView) {
      ButterKnife.bind(this, itemView);
    }
  }
}
