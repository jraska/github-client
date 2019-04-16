package com.jraska.github.client.users.ui

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.jraska.github.client.users.R
import com.jraska.github.client.users.UserStats
import kotlinx.android.synthetic.main.item_user_stats.view.user_detail_followers_count
import kotlinx.android.synthetic.main.item_user_stats.view.user_detail_following_count
import kotlinx.android.synthetic.main.item_user_stats.view.user_detail_joined
import kotlinx.android.synthetic.main.item_user_stats.view.user_detail_repos_count
import org.threeten.bp.format.DateTimeFormatter

internal class UserHeaderModel(private val basicStats: UserStats) : EpoxyModel<View>() {

  override fun getDefaultLayout(): Int {
    return R.layout.item_user_stats
  }

  override fun bind(itemView: View) {
    itemView.user_detail_followers_count.text = basicStats.followers.toString()
    itemView.user_detail_following_count.text = basicStats.following.toString()
    itemView.user_detail_repos_count.text = basicStats.publicRepos.toString()

    val joinedDateText = JOINED_FORMAT.format(basicStats.joined)
    val joinedText = itemView.context
      .getString(R.string.user_detail_joined_template, joinedDateText)
    itemView.user_detail_joined.text = joinedText
  }

  companion object {
    internal val JOINED_FORMAT = DateTimeFormatter.ISO_INSTANT
  }
}
