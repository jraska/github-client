package com.jraska.github.client.users.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.airbnb.epoxy.SimpleEpoxyModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.viewModel
import com.jraska.github.client.users.R
import com.jraska.github.client.users.model.RepoDetail
import com.jraska.github.client.users.model.RepoDetailViewModel

internal class RepoDetailActivity : BaseActivity() {

  private val viewModel: RepoDetailViewModel by lazy { viewModel(RepoDetailViewModel::class.java) }

  private val repoDetailRecycler: RecyclerView get() = findViewById(R.id.repo_detail_recycler)

  private fun fullRepoName(): String {
    return intent.getStringExtra(EXTRA_FULL_REPO_NAME)!!
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_repo_detail)
    setSupportActionBar(findViewById(R.id.toolbar))
    repoDetailRecycler.layoutManager = LinearLayoutManager(this)

    title = fullRepoName()

    val liveData = viewModel.repoDetail(fullRepoName())
    liveData.observe(this, { this.setState(it) })

    findViewById<FloatingActionButton>(R.id.repo_detail_github_fab)
      .setOnClickListener { viewModel.onGitHubIconClicked(fullRepoName()) }
  }

  private fun setState(state: RepoDetailViewModel.ViewState) {
    when (state) {
      is RepoDetailViewModel.ViewState.Loading -> showLoading()
      is RepoDetailViewModel.ViewState.Error -> setError(state.error)
      is RepoDetailViewModel.ViewState.ShowRepo -> setRepoDetail(state.repo)
    }
  }

  private fun showLoading() {
    findViewById<RecyclerView>(R.id.repo_detail_recycler).adapter = SimpleEpoxyAdapter().apply { addModels(SimpleEpoxyModel(R.layout.item_loading)) }
  }

  private fun setError(error: Throwable) {
    ErrorHandler.displayError(error, repoDetailRecycler)
  }

  private fun setRepoDetail(repoDetail: RepoDetail) {
    val adapter = SimpleEpoxyAdapter()
    adapter.addModels(RepoDetailHeaderModel(repoDetail))

    val languageText = getString(
      R.string.repo_detail_language_used_template,
      repoDetail.data.language
    )
    adapter.addModels(SimpleTextModel(languageText))

    val issuesText = getString(
      R.string.repo_detail_issues_template,
      repoDetail.data.issuesCount.toString()
    )
    adapter.addModels(SimpleTextModel(issuesText))

    repoDetailRecycler.adapter = adapter
  }

  companion object {
    private const val EXTRA_FULL_REPO_NAME = "fullRepoName"

    fun start(from: Activity, fullRepoName: String) {
      val intent = Intent(from, RepoDetailActivity::class.java)
      intent.putExtra(EXTRA_FULL_REPO_NAME, fullRepoName)

      from.startActivity(intent)
    }
  }
}
