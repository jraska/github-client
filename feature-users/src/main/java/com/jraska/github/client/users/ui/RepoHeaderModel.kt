package com.jraska.github.client.users.ui

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.jraska.github.client.users.R
import com.jraska.github.client.users.RepoHeader
import kotlinx.android.synthetic.main.item_row_user_detail_repo.view.repo_item_description
import kotlinx.android.synthetic.main.item_row_user_detail_repo.view.repo_item_forks
import kotlinx.android.synthetic.main.item_row_user_detail_repo.view.repo_item_stars
import kotlinx.android.synthetic.main.item_row_user_detail_repo.view.repo_item_title

internal class RepoHeaderModel(
  private val repo: RepoHeader,
  private val repoListener: RepoListener
) : EpoxyModel<View>() {
  override fun getDefaultLayout(): Int {
    return R.layout.item_row_user_detail_repo
  }

  override fun bind(itemView: View) {
    itemView.repo_item_title.text = repo.name
    itemView.repo_item_description.text = repo.description
    itemView.repo_item_stars.text = repo.stars.toString()
    itemView.repo_item_forks.text = repo.forks.toString()

    itemView.setOnClickListener { repoListener.onRepoClicked(repo) }
  }

  internal interface RepoListener {
    fun onRepoClicked(header: RepoHeader)
  }
}
