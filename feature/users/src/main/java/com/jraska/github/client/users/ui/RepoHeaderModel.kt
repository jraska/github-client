package com.jraska.github.client.users.ui

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import com.jraska.github.client.users.R
import com.jraska.github.client.users.model.RepoHeader

internal class RepoHeaderModel(
  private val repo: RepoHeader,
  private val repoListener: (RepoHeader) -> Unit
) : EpoxyModel<View>() {
  override fun getDefaultLayout(): Int {
    return R.layout.item_row_user_detail_repo
  }

  override fun bind(itemView: View) {
    itemView.findViewById<TextView>(R.id.repo_item_title).text = repo.name
    itemView.findViewById<TextView>(R.id.repo_item_description).text = repo.description
    itemView.findViewById<TextView>(R.id.repo_item_stars).text = repo.stars.toString()
    itemView.findViewById<TextView>(R.id.repo_item_forks).text = repo.forks.toString()

    itemView.setOnClickListener { repoListener(repo) }
  }
}
