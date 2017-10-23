package com.jraska.github.client.ui

import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.jraska.github.client.R
import com.jraska.github.client.users.UserStats
import org.threeten.bp.format.DateTimeFormatter

internal class UserHeaderModel(private val basicStats: UserStats) : EpoxyModelWithHolder<UserHeaderModel.HeaderHolder>() {

  override fun createNewHolder(): HeaderHolder {
    return HeaderHolder()
  }

  override fun getDefaultLayout(): Int {
    return R.layout.item_user_stats
  }

  override fun bind(holder: HeaderHolder) {
    holder.followersTextView.text = basicStats.followers.toString()
    holder.followingTextView.text = basicStats.following.toString()
    holder.reposCountTextView.text = basicStats.publicRepos.toString()

    val joinedDateText = JOINED_FORMAT.format(basicStats.joined)
    val joinedText = holder.joinedTextView.context
      .getString(R.string.user_detail_joined_template, joinedDateText)
    holder.joinedTextView.text = joinedText
  }

  internal class HeaderHolder : EpoxyHolder() {
    @BindView(R.id.user_detail_repos_count) lateinit var reposCountTextView: TextView
    @BindView(R.id.user_detail_following_count) lateinit var followingTextView: TextView
    @BindView(R.id.user_detail_followers_count) lateinit var followersTextView: TextView
    @BindView(R.id.user_detail_joined) lateinit var joinedTextView: TextView

    override fun bindView(itemView: View) {
      ButterKnife.bind(this, itemView)
    }
  }

  companion object {
    internal val JOINED_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME
  }
}
