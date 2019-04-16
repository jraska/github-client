package com.jraska.github.client.users.ui

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.jraska.github.client.core.android.visible
import com.jraska.github.client.users.R
import com.jraska.github.client.users.User
import kotlinx.android.synthetic.main.item_row_user.view.user_admin_image
import kotlinx.android.synthetic.main.item_row_user.view.user_avatar
import kotlinx.android.synthetic.main.item_row_user.view.user_container
import kotlinx.android.synthetic.main.item_row_user.view.user_item_gitHub_icon
import kotlinx.android.synthetic.main.item_row_user.view.user_login

internal class UserModel(private val user: User, private val userListener: UserListener) : EpoxyModel<View>() {

  override fun bind(itemView: View) {
    itemView.user_login.text = user.login
    itemView.user_avatar.setImageURI(user.avatarUrl)
    itemView.user_admin_image.visible(user.isAdmin)
    itemView.user_item_gitHub_icon.setOnClickListener { userListener.onUserGitHubIconClicked(user) }
    itemView.user_container.setOnClickListener { userListener.onUserClicked(user) }
  }

  override fun getDefaultLayout(): Int {
    return R.layout.item_row_user
  }

  internal interface UserListener {
    fun onUserClicked(user: User)

    fun onUserGitHubIconClicked(user: User)
  }
}
