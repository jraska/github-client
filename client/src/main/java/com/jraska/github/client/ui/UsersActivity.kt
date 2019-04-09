package com.jraska.github.client.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.jraska.github.client.R
import com.jraska.github.client.users.User
import com.jraska.github.client.users.UsersViewModel
import com.jraska.github.client.viewModel

class UsersActivity : BaseActivity(), UserModel.UserListener {
  private lateinit var usersViewModel: UsersViewModel

  @BindView(R.id.users_refresh_swipe_layout) lateinit var swipeRefreshLayout: SwipeRefreshLayout
  @BindView(R.id.users_recycler) lateinit var usersRecyclerView: RecyclerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_users_list)

    usersViewModel = viewModel(UsersViewModel::class.java)

    usersRecyclerView.layoutManager = LinearLayoutManager(this)
    swipeRefreshLayout.setOnRefreshListener { usersViewModel.onRefresh() }

    showProgressIndicator()

    usersViewModel.users().observe(this, Observer { this.setState(it) })
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.users_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_settings -> {
        usersViewModel.onSettingsIconClicked()
        return true
      }

      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onUserClicked(user: User) {
    usersViewModel.onUserClicked(user)
  }

  override fun onUserGitHubIconClicked(user: User) {
    usersViewModel.onUserGitHubIconClicked(user)
  }

  private fun setState(state: UsersViewModel.ViewState) {
    when(state) {
      is UsersViewModel.ViewState.Loading -> showProgressIndicator()
      else -> hideProgressIndicator()
    }

    when(state) {
      is UsersViewModel.ViewState.Error -> showError(state.error)
      is UsersViewModel.ViewState.ShowUsers -> setUsers(state.users)
    }
  }

  private fun setUsers(users: List<User>) {
    val adapter = SimpleEpoxyAdapter()
    val models = users.map { user -> UserModel(user, this) }

    adapter.addModels(models)
    usersRecyclerView.adapter = adapter
  }

  private fun showError(error: Throwable) {
    ErrorHandler.displayError(error, usersRecyclerView)
  }

  private fun showProgressIndicator() {
    ensureProgressIndicatorVisible()

    swipeRefreshLayout.isRefreshing = true
  }

  private fun hideProgressIndicator() {
    swipeRefreshLayout.isRefreshing = false
  }

  private fun ensureProgressIndicatorVisible() {
    // Workaround for start progress called before onMeasure
    // Issue: https://code.google.com/p/android/issues/detail?id=77712
    if (swipeRefreshLayout.measuredHeight == 0) {
      val circleSize = resources.getDimensionPixelSize(R.dimen.swipe_progress_circle_diameter)
      swipeRefreshLayout.setProgressViewOffset(false, 0, circleSize)
    }
  }

  companion object {
    fun start(inActivity: Activity) {
      val intent = Intent(inActivity, UsersActivity::class.java)
      inActivity.startActivity(intent)
    }
  }
}
