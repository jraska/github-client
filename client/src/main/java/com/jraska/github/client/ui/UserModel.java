package com.jraska.github.client.ui;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jraska.github.client.R;
import com.jraska.github.client.users.User;

class UserModel extends EpoxyModelWithHolder<UserModel.UserHolder> {
  private final User user;
  private final UserListener userListener;

  public UserModel(User user, UserListener userListener) {
    this.user = user;
    this.userListener = userListener;
  }

  @Override protected UserHolder createNewHolder() {
    return new UserHolder();
  }

  @Override public void bind(UserHolder holder) {
    holder.loginTextView.setText(user.getLogin());
//    holder.avatarView.setImageDrawable(null);
    holder.avatarView.setImageURI(user.getAvatarUrl());

    if (user.isAdmin()) {
      holder.adminView.setVisibility(View.VISIBLE);
    } else {
      holder.adminView.setVisibility(View.GONE);
    }

    holder.iconView.setOnClickListener(v -> userListener.onUserGitHubIconClicked(user));
    holder.containerView.setOnClickListener(v -> userListener.onUserClicked(user));
  }

  @Override protected int getDefaultLayout() {
    return R.layout.item_row_user;
  }

  static class UserHolder extends EpoxyHolder {
    @BindView(R.id.user_login) TextView loginTextView;
    @BindView(R.id.user_avatar) SimpleDraweeView avatarView;
    @BindView(R.id.user_admin_image) View adminView;
    @BindView(R.id.user_container) View containerView;
    @BindView(R.id.user_item_gitHub_icon) View iconView;

    @Override protected void bindView(View itemView) {
      ButterKnife.bind(this, itemView);
    }
  }

  interface UserListener {
    void onUserClicked(User user);

    void onUserGitHubIconClicked(User user);
  }
}
