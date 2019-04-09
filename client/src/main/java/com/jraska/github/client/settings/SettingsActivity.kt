package com.jraska.github.client.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.jraska.github.client.R
import com.jraska.github.client.ui.BaseActivity
import com.jraska.github.client.viewModel

class SettingsActivity : BaseActivity(), PurchaseReportModel.PurchaseListener {
  @BindView(R.id.settings_recycler) lateinit var recyclerView: RecyclerView

  private lateinit var viewModel: SettingsViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)

    viewModel = viewModel(SettingsViewModel::class.java)

    recyclerView.layoutManager = LinearLayoutManager(this)
    val adapter = SimpleEpoxyAdapter()
    adapter.addModels(PurchaseReportModel(this))
    adapter.addModels(ConsoleModel())
    recyclerView.adapter = adapter
  }

  override fun onPurchaseButtonClicked(value: String) {
    viewModel.onPurchaseSubmitted(value)
  }

  companion object {
    fun start(inActivity: Activity) {
      val intent = Intent(inActivity, SettingsActivity::class.java)
      inActivity.startActivity(intent)
    }
  }
}
