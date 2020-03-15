package com.jraska.github.client.settings

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.google.android.play.core.splitcompat.SplitCompat
import com.jraska.github.client.DynamicFeaturesComponent
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.ViewModelFactory
import com.jraska.github.client.dynamicFeaturesComponent
import dagger.Component
import kotlinx.android.synthetic.main.activity_settings.toolbar
import kotlinx.android.synthetic.main.content_settings.settings_recycler

internal class SettingsActivity : BaseActivity() {
  private val viewModel: SettingsViewModel by lazy {
    ViewModelProvider(this, viewModelFactory()).get(SettingsViewModel::class.java)
  }

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(newBase)
    SplitCompat.install(this)
  }

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

  private fun viewModelFactory(): ViewModelProvider.Factory {
    return DaggerSettingsComponent.builder()
      .dynamicFeaturesComponent(dynamicFeaturesComponent())
      .build()
      .viewModelFactory()
  }
}

@Component(
  modules = [SettingsModule::class],
  dependencies = [DynamicFeaturesComponent::class]
)
internal interface SettingsComponent {
  fun viewModelFactory(): ViewModelFactory
}
