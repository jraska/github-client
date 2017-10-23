package com.jraska.github.client.ui

import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.jraska.github.client.R
import com.jraska.github.client.users.RepoDetail
import org.threeten.bp.format.DateTimeFormatter

internal class RepoDetailHeaderModel(private val repoDetail: RepoDetail)
  : EpoxyModelWithHolder<RepoDetailHeaderModel.Holder>() {

  override fun getDefaultLayout(): Int {
    return R.layout.item_repo_detail_stats
  }

  override fun createNewHolder(): Holder {
    return Holder()
  }

  override fun bind(holder: Holder) {
    val createdText = CREATED_DATE_FORMAT.format(repoDetail.data.created)
    holder.createdTextView.text = createdText

    holder.subscribersTextView.text = repoDetail.data.subscribersCount.toString()
    holder.forksTextView.text = repoDetail.header.forks.toString()
    holder.starsTextView.text = repoDetail.header.stars.toString()
  }

  internal class Holder : EpoxyHolder() {
    @BindView(R.id.repo_detail_created) lateinit var createdTextView: TextView
    @BindView(R.id.repo_detail_subscribers_count) lateinit var subscribersTextView: TextView
    @BindView(R.id.repo_detail_stars_count) lateinit var starsTextView: TextView
    @BindView(R.id.repo_detail_forks_count) lateinit var forksTextView: TextView

    override fun bindView(view: View) {
      ButterKnife.bind(this, view)
    }
  }

  companion object {
    val CREATED_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME
  }
}
