package com.jraska.github.client.users.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.airbnb.epoxy.SimpleEpoxyModel
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.viewModel
import com.jraska.github.client.users.R
import com.jraska.github.client.users.RepoHeader
import com.jraska.github.client.users.UserDetail
import com.jraska.github.client.users.UserDetailViewModel
import kotlinx.android.synthetic.main.activity_user_detail.toolbar
import kotlinx.android.synthetic.main.activity_user_detail.user_detail_avatar
import kotlinx.android.synthetic.main.activity_user_detail.user_detail_github_fab
import kotlinx.android.synthetic.main.content_user_detail.user_detail_recycler

class UserDetailActivity : BaseActivity(), RepoHeaderModel.RepoListener {

  private val userDetailViewModel: UserDetailViewModel by lazy { viewModel(UserDetailViewModel::class.java) }

  fun login(): String {
    return intent.getStringExtra(EXTRA_USER_LOGIN)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_user_detail)
    setSupportActionBar(toolbar)

    user_detail_recycler.layoutManager = LinearLayoutManager(this)
    user_detail_recycler.isNestedScrollingEnabled = false

    title = login()

    val detailLiveData = userDetailViewModel.userDetail(login())
    detailLiveData.observe(this, Observer { this.setState(it) })

    user_detail_github_fab.setOnClickListener { userDetailViewModel.onUserGitHubIconClick(login()) }
  }

  private fun setState(state: UserDetailViewModel.ViewState) {
    when (state) {
      is UserDetailViewModel.ViewState.Loading -> showLoading()
      is UserDetailViewModel.ViewState.Error -> showError(state.error)
      is UserDetailViewModel.ViewState.DisplayUser -> setUser(state.user)
    }
  }

  private fun setUser(userDetail: UserDetail) {
    user_detail_avatar.setImageURI(userDetail.user.avatarUrl)

    if (userDetail.basicStats == null) {
      return
    }

    val adapter = SimpleEpoxyAdapter()

    val models = ArrayList<EpoxyModel<*>>()
    models.add(UserHeaderModel(userDetail.basicStats))

    if (!userDetail.popularRepos.isEmpty()) {
      models.add(ReposSectionModel(getString(R.string.repos_popular), userDetail.popularRepos, this))
    }

    if (!userDetail.contributedRepos.isEmpty()) {
      models.add(ReposSectionModel(getString(R.string.repos_contributed), userDetail.contributedRepos, this))
    }

    adapter.addModels(models)

    user_detail_recycler.adapter = adapter
  }

  private fun showLoading() {
    user_detail_recycler.adapter = SimpleEpoxyAdapter().apply { addModels(SimpleEpoxyModel(R.layout.item_loading)) }
  }

  private fun showError(error: Throwable) {
    ErrorHandler.displayError(error, user_detail_recycler)
  }

  override fun onRepoClicked(header: RepoHeader) {
    userDetailViewModel.onRepoClicked(header)
  }

  companion object {
    internal const val EXTRA_USER_LOGIN = "login"

    fun start(inActivity: Activity, login: String) {
      val intent = Intent(inActivity, UserDetailActivity::class.java)
      intent.putExtra(EXTRA_USER_LOGIN, login)

      inActivity.startActivity(intent)
    }
  }
}
