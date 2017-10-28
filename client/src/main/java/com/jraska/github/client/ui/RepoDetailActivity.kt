package com.jraska.github.client.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.jraska.github.client.R
import com.jraska.github.client.users.RepoDetail
import com.jraska.github.client.users.RepoDetailViewModel
import com.jraska.github.client.viewModel

class RepoDetailActivity : BaseActivity() {

  @BindView(R.id.repo_detail_recycler) internal lateinit var recyclerView: RecyclerView

  private lateinit var viewModel: RepoDetailViewModel

  private fun fullRepoName(): String {
    return intent.getStringExtra(EXTRA_FULL_REPO_NAME)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_repo_detail)
    recyclerView.layoutManager = LinearLayoutManager(this)

    title = fullRepoName()

    viewModel = viewModel(RepoDetailViewModel::class.java)
    val liveData = viewModel.repoDetail(fullRepoName())
    liveData.observe(this, Observer { this.setState(it!!) })
  }

  @OnClick(R.id.repo_detail_github_fab) internal fun onFitHubIconClicked() {
    viewModel.onFitHubIconClicked(fullRepoName())
  }

  internal fun setState(viewState: RepoDetailViewModel.ViewState) {
    if (viewState.repoDetail != null) {
      setRepoDetail(viewState.repoDetail!!)
    }

    if (viewState.error != null) {
      setError(viewState.error!!)
    }
  }

  internal fun setError(error: Throwable) {
    ErrorHandler.displayError(error, recyclerView)
  }

  internal fun setRepoDetail(repoDetail: RepoDetail) {
    val adapter = SimpleEpoxyAdapter()
    adapter.addModels(RepoDetailHeaderModel(repoDetail))

    val languageText = getString(R.string.repo_detail_language_used_template,
      repoDetail.data.language)
    adapter.addModels(SimpleTextModel(languageText))

    val issuesText = getString(R.string.repo_detail_issues_template,
      repoDetail.data.issuesCount.toString())
    adapter.addModels(SimpleTextModel(issuesText))

    recyclerView.adapter = adapter
  }

  companion object {
    private val EXTRA_FULL_REPO_NAME = "fullRepoName"

    fun start(from: Activity, fullRepoName: String) {
      val intent = Intent(from, RepoDetailActivity::class.java)
      intent.putExtra(EXTRA_FULL_REPO_NAME, fullRepoName)

      from.startActivity(intent)
    }
  }
}
