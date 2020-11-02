package com.jraska.github.client.users.ui

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import com.facebook.drawee.view.SimpleDraweeView
import com.jraska.github.client.users.R
import com.jraska.github.client.users.model.User

internal class UserModel(private val user: User, private val userListener: UserListener) : EpoxyModel<View>() {

  override fun bind(itemView: View) {
    itemView.findViewById<TextView>(R.id.user_login).text = user.login
    itemView.findViewById<SimpleDraweeView>(R.id.user_avatar).setImageURI(user.avatarUrl)
    itemView.findViewById<View>(R.id.user_admin_image).visibility = if (user.isAdmin) {
      View.VISIBLE
    } else {
      View.GONE
    }
    itemView.findViewById<View>(R.id.user_item_gitHub_icon).setOnClickListener { userListener.onUserGitHubIconClicked(user) }
    itemView.findViewById<View>(R.id.user_container).setOnClickListener { userListener.onUserClicked(user) }
  }

  override fun getDefaultLayout(): Int {
    return R.layout.item_row_user
  }

  internal interface UserListener {
    fun onUserClicked(user: User)

    fun onUserGitHubIconClicked(user: User)
  }
}
