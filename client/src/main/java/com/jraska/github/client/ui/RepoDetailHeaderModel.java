package com.jraska.github.client.ui;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.jraska.github.client.R;
import com.jraska.github.client.users.RepoDetail;
import org.threeten.bp.format.DateTimeFormatter;

final class RepoDetailHeaderModel extends EpoxyModelWithHolder<RepoDetailHeaderModel.Holder> {
  static final DateTimeFormatter CREATED_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  private final RepoDetail repoDetail;

  public RepoDetailHeaderModel(RepoDetail repoDetail) {
    this.repoDetail = repoDetail;
  }

  @Override protected int getDefaultLayout() {
    return R.layout.item_repo_detail_stats;
  }

  @Override protected Holder createNewHolder() {
    return new Holder();
  }

  @Override public void bind(Holder holder) {
    String createdText = CREATED_DATE_FORMAT.format(repoDetail.data.created);
    holder.createdTextView.setText(createdText);

    holder.subscribersTextView.setText(String.valueOf(repoDetail.data.subscribersCount));
    holder.forksTextView.setText(String.valueOf(repoDetail.header.forks));
    holder.starsTextView.setText(String.valueOf(repoDetail.header.stars));
  }

  static class Holder extends EpoxyHolder {
    @BindView(R.id.repo_detail_created) TextView createdTextView;
    @BindView(R.id.repo_detail_subscribers_count) TextView subscribersTextView;
    @BindView(R.id.repo_detail_stars_count) TextView starsTextView;
    @BindView(R.id.repo_detail_forks_count) TextView forksTextView;

    @Override protected void bindView(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
