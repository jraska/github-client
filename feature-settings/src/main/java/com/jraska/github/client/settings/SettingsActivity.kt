package com.jraska.github.client.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.viewModel
import kotlinx.android.synthetic.main.activity_settings.toolbar
import kotlinx.android.synthetic.main.content_settings.settings_recycler

internal class SettingsActivity : BaseActivity() {
  private val viewModel: SettingsViewModel by lazy { viewModel(SettingsViewModel::class.java) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    setSupportActionBar(toolbar)

    settings_recycler.layoutManager = LinearLayoutManager(this)
    val adapter = SimpleEpoxyAdapter()
    adapter.addModels(PurchaseReportModel(this::onPurchaseButtonClicked))
    adapter.addModels(ConsoleModel())
    settings_recycler.adapter = adapter
  }

  private fun onPurchaseButtonClicked(value: String) {
    viewModel.onPurchaseSubmitted(value)
  }

  companion object {
    fun start(inActivity: Activity) {
      val intent = Intent(inActivity, SettingsActivity::class.java)
      inActivity.startActivity(intent)
    }
  }
}
