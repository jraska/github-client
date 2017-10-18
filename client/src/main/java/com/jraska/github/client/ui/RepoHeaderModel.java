package com.jraska.github.client.ui;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.jraska.github.client.R;
import com.jraska.github.client.common.Preconditions;
import com.jraska.github.client.users.RepoHeader;
import io.reactivex.annotations.Nullable;

public class RepoHeaderModel extends EpoxyModelWithHolder<RepoHeaderModel.RepoHolder> {
  private final RepoHeader repo;
  private final View.OnClickListener itemClickListener;

  public RepoHeaderModel(RepoHeader repo, RepoListener repoListener) {
    Preconditions.argNotNull(repoListener);

    this.repo = Preconditions.argNotNull(repo);
    this.itemClickListener = (v) -> repoListener.onRepoClicked(repo);
  }

  @Override protected RepoHolder createNewHolder() {
    return new RepoHolder();
  }

  @Override protected int getDefaultLayout() {
    return R.layout.item_row_user_detail_repo;
  }

  @Override public void bind(RepoHolder holder) {
    holder.titleTextView.setText(repo.getName());
    holder.descriptionTextView.setText(repo.getDescription());
    holder.starsTextView.setText(String.valueOf(repo.getStars()));
    holder.forksTextView.setText(String.valueOf(repo.getForks()));

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
