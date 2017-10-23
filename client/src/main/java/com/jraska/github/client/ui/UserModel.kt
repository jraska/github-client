package com.jraska.github.client.ui

import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.facebook.drawee.view.SimpleDraweeView
import com.jraska.github.client.R
import com.jraska.github.client.users.User

internal class UserModel(private val user: User, private val userListener: UserListener) : EpoxyModelWithHolder<UserModel.UserHolder>() {

  override fun createNewHolder(): UserHolder {
    return UserHolder()
  }

  override fun bind(holder: UserHolder) {
    holder.loginTextView.text = user.login
    //    holder.avatarView.setImageDrawable(null);
    holder.avatarView.setImageURI(user.avatarUrl)

    if (user.isAdmin) {
      holder.adminView.visibility = View.VISIBLE
    } else {
      holder.adminView.visibility = View.GONE
    }

    holder.iconView.setOnClickListener { v -> userListener.onUserGitHubIconClicked(user) }
    holder.containerView.setOnClickListener { v -> userListener.onUserClicked(user) }
  }

  override fun getDefaultLayout(): Int {
    return R.layout.item_row_user
  }

  internal class UserHolder : EpoxyHolder() {
    @BindView(R.id.user_login) lateinit var loginTextView: TextView
    @BindView(R.id.user_avatar) lateinit var avatarView: SimpleDraweeView
    @BindView(R.id.user_admin_image) lateinit var adminView: View
    @BindView(R.id.user_container) lateinit var containerView: View
    @BindView(R.id.user_item_gitHub_icon) lateinit var iconView: View

    override fun bindView(itemView: View) {
      ButterKnife.bind(this, itemView)
    }
  }

  internal interface UserListener {
    fun onUserClicked(user: User)

    fun onUserGitHubIconClicked(user: User)
  }
}
