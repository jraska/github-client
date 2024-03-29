package com.jraska.github.client.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.jraska.github.client.core.android.viewModel

internal class SettingsActivity : AppCompatActivity() {
  private val viewModel: SettingsViewModel by lazy { viewModel(SettingsViewModel::class.java) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    setSupportActionBar(findViewById(R.id.toolbar))

    val fragment = supportFragmentManager.findFragmentById(R.id.settings_fragment_google_login)
    if (fragment == null) {
      supportFragmentManager.beginTransaction()
        .add(R.id.settings_fragment_google_login, viewModel.signInFactory.signInFragment())
        .commitNow()
    }

    val settingsRecycler = findViewById<RecyclerView>(R.id.settings_recycler)
    settingsRecycler.layoutManager = LinearLayoutManager(this)
    val adapter = SimpleEpoxyAdapter()

    adapter.addModels(IntegrityCheckModel(viewModel::onIntegrityCheckClicked))
    adapter.addModels(PurchaseReportModel(this::onPurchaseButtonClicked))
    adapter.addModels(ConsoleModel(viewModel::onConsoleClick))

    viewModel.configRows().forEach {
      adapter.addModels(it as EpoxyModel<*>)
    }

    settingsRecycler.adapter = adapter
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
