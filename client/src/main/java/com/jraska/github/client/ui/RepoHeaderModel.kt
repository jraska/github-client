package com.jraska.github.client.ui

import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.jraska.github.client.R
import com.jraska.github.client.users.RepoHeader

internal class RepoHeaderModel(private val repo: RepoHeader, repoListener: RepoListener)
  : EpoxyModelWithHolder<RepoHeaderModel.RepoHolder>() {
  private val itemClickListener: View.OnClickListener

  init {
    this.itemClickListener = View.OnClickListener { repoListener.onRepoClicked(repo) }
  }

  override fun createNewHolder(): RepoHolder {
    return RepoHolder()
  }

  override fun getDefaultLayout(): Int {
    return R.layout.item_row_user_detail_repo
  }

  override fun bind(holder: RepoHolder) {
    holder.titleTextView.text = repo.name
    holder.descriptionTextView.text = repo.description
    holder.starsTextView.text = repo.stars.toString()
    holder.forksTextView.text = repo.forks.toString()

    holder.itemView.setOnClickListener(itemClickListener)
  }

  internal class RepoHolder : EpoxyHolder() {
    lateinit var itemView: View

    @BindView(R.id.repo_item_title) lateinit var titleTextView: TextView
    @BindView(R.id.repo_item_description) lateinit var descriptionTextView: TextView
    @BindView(R.id.repo_item_stars) lateinit var starsTextView: TextView
    @BindView(R.id.repo_item_forks) lateinit var forksTextView: TextView

    override fun bindView(itemView: View) {
      ButterKnife.bind(this, itemView)
      this.itemView = itemView
    }
  }

  internal interface RepoListener {
    fun onRepoClicked(header: RepoHeader)
  }
}
