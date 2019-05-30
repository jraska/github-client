package com.jraska.github.client.about

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.viewModel
import kotlinx.android.synthetic.main.activity_about.toolbar
import kotlinx.android.synthetic.main.content_about.about_recycler

internal class AboutActivity : BaseActivity() {

  private val viewModel: AboutViewModel by lazy { viewModel(AboutViewModel::class.java) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_about)
    setSupportActionBar(toolbar)

    about_recycler.layoutManager = LinearLayoutManager(this)
    about_recycler.adapter = SimpleEpoxyAdapter().apply {
      addModels(createModels())
    }
  }

  private fun createModels(): List<EpoxyModel<*>>? {
    return listOf(DescriptionModel(viewModel::onProjectDescriptionClick))
  }

  companion object {
    fun start(inActivity: Activity) {
      val intent = Intent(inActivity, AboutActivity::class.java)
      inActivity.startActivity(intent)
    }
  }
}
