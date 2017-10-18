package com.jraska.github.client.ui;

import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.jraska.github.client.R;
import com.jraska.github.client.users.UserStats;

import org.threeten.bp.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserHeaderModel extends EpoxyModelWithHolder<UserHeaderModel.HeaderHolder> {
  static final DateTimeFormatter JOINED_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  private final UserStats basicStats;

  public UserHeaderModel(UserStats basicStats) {
    this.basicStats = basicStats;
  }

  @Override protected HeaderHolder createNewHolder() {
    return new HeaderHolder();
  }

  @Override protected int getDefaultLayout() {
    return R.layout.item_user_stats;
  }

  @Override public void bind(HeaderHolder holder) {
    holder.followersTextView.setText(String.valueOf(basicStats.getFollowers()));
    holder.followingTextView.setText(String.valueOf(basicStats.getFollowing()));
    holder.reposCountTextView.setText(String.valueOf(basicStats.getPublicRepos()));

    String joinedDateText = JOINED_FORMAT.format(basicStats.getJoined());
    String joinedText = holder.joinedTextView.getContext()
        .getString(R.string.user_detail_joined_template, joinedDateText);
    holder.joinedTextView.setText(joinedText);
  }

  static class HeaderHolder extends EpoxyHolder {
    @BindView(R.id.user_detail_repos_count) TextView reposCountTextView;
    @BindView(R.id.user_detail_following_count) TextView followingTextView;
    @BindView(R.id.user_detail_followers_count) TextView followersTextView;
    @BindView(R.id.user_detail_joined) TextView joinedTextView;

    @Override protected void bindView(View itemView) {
      ButterKnife.bind(this, itemView);
    }
  }
}
