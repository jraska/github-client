package com.jraska.github.client.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.airbnb.epoxy.SimpleEpoxyModel
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

  private fun setState(state: RepoDetailViewModel.ViewState) {
    when (state) {
      is RepoDetailViewModel.ViewState.Loading -> showLoading()
      is RepoDetailViewModel.ViewState.Error -> setError(state.error)
      is RepoDetailViewModel.ViewState.ShowRepo -> setRepoDetail(state.repo)
    }
  }

  private fun showLoading() {
    recyclerView.adapter = SimpleEpoxyAdapter().apply { addModels(SimpleEpoxyModel(R.layout.item_loading)) }
  }

  private fun setError(error: Throwable) {
    ErrorHandler.displayError(error, recyclerView)
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

    recyclerView.adapter = adapter
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
