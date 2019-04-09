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
import com.facebook.drawee.view.SimpleDraweeView
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
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
  private lateinit var loadTrace: Trace

  fun login(): String {
    return intent.getStringExtra(EXTRA_USER_LOGIN)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    loadTrace = FirebasePerformance.getInstance().newTrace("user_detail_load")
    loadTrace.start()

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_user_detail)

    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.isNestedScrollingEnabled = false

    title = login()

    userDetailViewModel = viewModel(UserDetailViewModel::class.java)

    val detailLiveData = userDetailViewModel.userDetail(login())
    detailLiveData.observe(this, Observer { this.setState(it!!) })
  }

  @OnClick(R.id.user_detail_github_fab) internal fun gitHubFabClicked() {
    userDetailViewModel.onUserGitHubIconClick(login())
  }

  private fun setState(viewState: UserDetailViewModel.ViewState) {
    if (viewState.isLoading) {
      // TODO(josef): Show some progress
    }

    if (viewState.result() != null) {
      setUser(viewState.result()!!)
    }

    if (viewState.error() != null) {
      showError(viewState.error()!!)
    }
  }

  internal fun setUser(userDetail: UserDetail) {
    loadTrace.incrementMetric("set_user_detail", 1)
    avatarView.setImageURI(userDetail.user.avatarUrl)

    if (userDetail.basicStats == null) {
      loadTrace.incrementMetric("set_user_detail_incomplete", 1)
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
    loadTrace.stop()
  }

  fun showError(error: Throwable) {
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
