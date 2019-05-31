package com.jraska.github.client.about

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.viewModel
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.content_about.*

internal class AboutActivity : BaseActivity() {

  private val viewModel: AboutViewModel by lazy { viewModel(AboutViewModel::class.java) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_about)
    setSupportActionBar(toolbar)

    val spansCount = 2
    val epoxyAdapter = SimpleEpoxyAdapter()
    epoxyAdapter.spanCount = spansCount
    epoxyAdapter.addModels(createModels())

    val layoutManager = GridLayoutManager(this, spansCount)
    layoutManager.spanSizeLookup = epoxyAdapter.spanSizeLookup

    about_recycler.layoutManager = layoutManager
    about_recycler.adapter = epoxyAdapter
  }

  private fun createModels(): List<EpoxyModel<*>>? {
    return listOf(
      DescriptionModel(viewModel::onProjectDescriptionClick),
      IconModel(viewModel::onGithubClick, R.drawable.ic_github_48dp, R.string.about_github_description),
      IconModel(viewModel::onWebClick, R.drawable.ic_web_48dp, R.string.about_web_description),
      IconModel(viewModel::onMediumClick, R.drawable.ic_medium_48dp, R.string.about_medium_description),
      IconModel(viewModel::onTwitterClick, R.drawable.ic_twitter_logo_blue_48dp, R.string.about_twitter_description)
    )
  }

  companion object {
    fun start(inActivity: Activity) {
      val intent = Intent(inActivity, AboutActivity::class.java)
      inActivity.startActivity(intent)
    }
  }
}
