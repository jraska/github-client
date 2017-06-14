package com.jraska.github.client.ui;

import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.jraska.github.client.R;
import com.jraska.github.client.users.RepoHeader;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

public class RepoHeaderModel extends EpoxyModelWithHolder<RepoHeaderModel.RepoHolder> {
  private final RepoHeader repo;

  @Nullable
  private final View.OnClickListener itemClickListener;

  public RepoHeaderModel(RepoHeader repo) {
    this(repo, null);
  }

  public RepoHeaderModel(RepoHeader repo, @Nullable RepoListener repoListener) {
    this.repo = repo;
    if (repoListener == null) {
      this.itemClickListener = null;
    } else
      this.itemClickListener = (v) -> repoListener.onRepoClicked(repo);
  }

  @Override protected RepoHolder createNewHolder() {
    return new RepoHolder();
  }

  @Override protected int getDefaultLayout() {
    return R.layout.item_row_user_detail_repo;
  }

  @Override public void bind(RepoHolder holder) {
    holder.titleTextView.setText(repo.name);
    holder.descriptionTextView.setText(repo.description);
    holder.starsTextView.setText(String.valueOf(repo.stars));
    holder.forksTextView.setText(String.valueOf(repo.forks));

    holder.itemView.setOnClickListener(itemClickListener);

  }

  static class RepoHolder extends EpoxyHolder {
    View itemView;

    @BindView(R.id.repo_item_title) TextView titleTextView;
    @BindView(R.id.repo_item_description) TextView descriptionTextView;
    @BindView(R.id.repo_item_stars) TextView starsTextView;
    @BindView(R.id.repo_item_forks) TextView forksTextView;

    @Override protected void bindView(View itemView) {
      ButterKnife.bind(this, itemView);
      this.itemView = itemView;
    }
  }

  interface RepoListener {
    void onRepoClicked(RepoHeader header);
  }
}
