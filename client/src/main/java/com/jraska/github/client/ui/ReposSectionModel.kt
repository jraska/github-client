package com.jraska.github.client.ui

import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.jraska.github.client.R
import com.jraska.github.client.users.RepoHeader
import com.jraska.github.client.widget.RepeaterLayout

internal class ReposSectionModel(
  private val title: String,
  private val repos: List<RepoHeader>,
  private val repoListener: RepoHeaderModel.RepoListener
) : EpoxyModelWithHolder<ReposSectionModel.ReposHolder>() {

  override fun createNewHolder(): ReposHolder {
    return ReposHolder()
  }

  override fun getDefaultLayout(): Int {
    return R.layout.item_repos_section
  }

  override fun bind(holder: ReposHolder) {
    holder.reposTitle.text = title

    val adapter = SimpleEpoxyAdapter()
    adapter.addModels(repos.map { repo -> RepoHeaderModel(repo, repoListener) })

    holder.reposRepeater.setAdapter(adapter)
  }

  internal class ReposHolder : EpoxyHolder() {
    @BindView(R.id.repos_repeater)
    lateinit var reposRepeater: RepeaterLayout
    @BindView(R.id.repos_title)
    lateinit var reposTitle: TextView

    override fun bindView(itemView: View) {
      ButterKnife.bind(this, itemView)
    }
  }
}
