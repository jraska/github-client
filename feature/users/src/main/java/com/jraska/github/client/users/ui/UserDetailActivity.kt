package com.jraska.github.client.users.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.airbnb.epoxy.SimpleEpoxyModel
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.viewModel
import com.jraska.github.client.users.R
import com.jraska.github.client.users.UserDetailViewModel
import com.jraska.github.client.users.model.RepoHeader
import com.jraska.github.client.users.model.UserDetail

internal class UserDetailActivity : BaseActivity() {

  private val userDetailViewModel: UserDetailViewModel by lazy { viewModel(UserDetailViewModel::class.java) }

  fun login(): String {
    return intent.getStringExtra(EXTRA_USER_LOGIN)!!
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_user_detail)
    setSupportActionBar(findViewById(R.id.toolbar))

    val userDetailRecycler = findViewById<RecyclerView>(R.id.user_detail_recycler)
    userDetailRecycler.layoutManager = LinearLayoutManager(this)
    userDetailRecycler.isNestedScrollingEnabled = false

    title = login()

    val detailLiveData = userDetailViewModel.userDetail(login())
    detailLiveData.observe(this, { this.setState(it) })

    findViewById<FloatingActionButton>(R.id.user_detail_github_fab).setOnClickListener { userDetailViewModel.onUserGitHubIconClick(login()) }
  }

  private fun setState(state: UserDetailViewModel.ViewState) {
    when (state) {
      is UserDetailViewModel.ViewState.Loading -> showLoading()
      is UserDetailViewModel.ViewState.Error -> showError(state.error)
      is UserDetailViewModel.ViewState.DisplayUser -> setUser(state.user)
    }
  }

  private fun setUser(userDetail: UserDetail) {
    findViewById<SimpleDraweeView>(R.id.user_detail_avatar).setImageURI(userDetail.user.avatarUrl)

    if (userDetail.basicStats == null) {
      return
    }

    val adapter = SimpleEpoxyAdapter()

    val models = ArrayList<EpoxyModel<*>>()
    models.add(UserHeaderModel(userDetail.basicStats))

    if (userDetail.popularRepos.isNotEmpty()) {
      models.add(ReposSectionModel(getString(R.string.repos_popular), userDetail.popularRepos, this::onRepoClicked))
    }

    if (userDetail.contributedRepos.isNotEmpty()) {
      models.add(
        ReposSectionModel(
          getString(R.string.repos_contributed),
          userDetail.contributedRepos,
          this::onRepoClicked
        )
      )
    }

    adapter.addModels(models)

    findViewById<RecyclerView>(R.id.user_detail_recycler).adapter = adapter
  }

  private fun showLoading() {
    findViewById<RecyclerView>(R.id.user_detail_recycler).adapter = SimpleEpoxyAdapter().apply { addModels(SimpleEpoxyModel(R.layout.item_loading)) }
  }

  private fun showError(error: Throwable) {
    ErrorHandler.displayError(error, findViewById(R.id.user_detail_recycler))
  }

  private fun onRepoClicked(header: RepoHeader) {
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
