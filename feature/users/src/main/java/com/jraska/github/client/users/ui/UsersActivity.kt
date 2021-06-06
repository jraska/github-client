package com.jraska.github.client.users.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.viewModel
import com.jraska.github.client.users.R
import com.jraska.github.client.users.UsersViewModel
import com.jraska.github.client.users.model.User

class UsersActivity : BaseActivity(), UserModel.UserListener {
  private val usersViewModel: UsersViewModel by lazy { viewModel(UsersViewModel::class.java) }

  private val usersRecycler: RecyclerView get() = findViewById(R.id.users_recycler)
  private val usersRefreshSwipeLayout: SwipeRefreshLayout get() = findViewById(R.id.users_refresh_swipe_layout)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_users_list)
    setSupportActionBar(findViewById(R.id.toolbar))

    usersRecycler.layoutManager = LinearLayoutManager(this)
    usersRefreshSwipeLayout.setOnRefreshListener { usersViewModel.onRefresh() }

    showProgressIndicator()

    usersViewModel.users().observe(this, { this.setState(it) })
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

      R.id.action_about -> {
        usersViewModel.onAboutIconClicked()
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
    when (state) {
      is UsersViewModel.ViewState.Loading -> showProgressIndicator()
      else -> hideProgressIndicator()
    }

    when (state) {
      is UsersViewModel.ViewState.Error -> showError(state.error)
      is UsersViewModel.ViewState.ShowUsers -> setUsers(state.users)
    }
  }

  private fun setUsers(users: List<User>) {
    val adapter = usersRecycler.adapter as SimpleEpoxyAdapter? ?: SimpleEpoxyAdapter()
    adapter.removeAllModels()

    val models = users.map { user -> UserModel(user, this) }

    adapter.addModels(models)
    usersRecycler.adapter = adapter
  }

  private fun showError(error: Throwable) {
    ErrorHandler.displayError(error, usersRecycler)
  }

  private fun showProgressIndicator() {
    ensureProgressIndicatorVisible()

    usersRefreshSwipeLayout.isRefreshing = true
  }

  private fun hideProgressIndicator() {
    usersRefreshSwipeLayout.isRefreshing = false
  }

  private fun ensureProgressIndicatorVisible() {
    // Workaround for start progress called before onMeasure
    // Issue: https://code.google.com/p/android/issues/detail?id=77712
    if (usersRefreshSwipeLayout.measuredHeight == 0) {
      val circleSize = resources.getDimensionPixelSize(R.dimen.swipe_progress_circle_diameter)
      usersRefreshSwipeLayout.setProgressViewOffset(false, 0, circleSize)
    }
  }

  companion object {
    fun start(inActivity: Activity) {
      val intent = Intent(inActivity, UsersActivity::class.java)
      inActivity.startActivity(intent)
    }
  }
}
