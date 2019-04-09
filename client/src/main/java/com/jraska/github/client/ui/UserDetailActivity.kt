package com.jraska.github.client.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.airbnb.epoxy.SimpleEpoxyModel
import com.facebook.drawee.view.SimpleDraweeView
import com.jraska.github.client.R
import com.jraska.github.client.users.RepoHeader
import com.jraska.github.client.users.UserDetail
import com.jraska.github.client.users.UserDetailViewModel
import com.jraska.github.client.viewModel
import java.util.ArrayList

class UserDetailActivity : BaseActivity(), RepoHeaderModel.RepoListener {

  @BindView(R.id.user_detail_avatar) lateinit var avatarView: SimpleDraweeView
  @BindView(R.id.user_detail_recycler) lateinit var recyclerView: RecyclerView

  private lateinit var userDetailViewModel: UserDetailViewModel

  fun login(): String {
    return intent.getStringExtra(EXTRA_USER_LOGIN)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_user_detail)

    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.isNestedScrollingEnabled = false

    title = login()

    userDetailViewModel = viewModel(UserDetailViewModel::class.java)

    val detailLiveData = userDetailViewModel.userDetail(login())
    detailLiveData.observe(this, Observer { this.setState(it) })
  }

  @OnClick(R.id.user_detail_github_fab) internal fun gitHubFabClicked() {
    userDetailViewModel.onUserGitHubIconClick(login())
  }

  private fun setState(state: UserDetailViewModel.ViewState) {
    when (state) {
      is UserDetailViewModel.ViewState.Loading -> showLoading()
      is UserDetailViewModel.ViewState.Error -> showError(state.error)
      is UserDetailViewModel.ViewState.DisplayUser -> setUser(state.user)
    }
  }

  private fun setUser(userDetail: UserDetail) {
    avatarView.setImageURI(userDetail.user.avatarUrl)

    if (userDetail.basicStats == null) {
      return
    }

    val adapter = SimpleEpoxyAdapter()

    val models = ArrayList<EpoxyModel<*>>()
    models.add(UserHeaderModel(userDetail.basicStats!!))

    if (!userDetail.popularRepos.isEmpty()) {
      models.add(ReposSectionModel(getString(R.string.repos_popular), userDetail.popularRepos, this))
    }

    if (!userDetail.contributedRepos.isEmpty()) {
      models.add(ReposSectionModel(getString(R.string.repos_contributed), userDetail.contributedRepos, this))
    }

    adapter.addModels(models)

    recyclerView.adapter = adapter
  }

  private fun showLoading() {
    recyclerView.adapter = SimpleEpoxyAdapter().apply { addModels(SimpleEpoxyModel(R.layout.item_loading)) }
  }

  private fun showError(error: Throwable) {
    ErrorHandler.displayError(error, recyclerView)
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
