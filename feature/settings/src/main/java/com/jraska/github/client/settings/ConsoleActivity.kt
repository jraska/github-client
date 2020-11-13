package com.jraska.github.client.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jraska.console.Console
import com.jraska.github.client.core.android.BaseActivity

internal class ConsoleActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(Console(this))
  }

  companion object {
    fun start(inActivity: Activity) {
      val intent = Intent(inActivity, ConsoleActivity::class.java)
      inActivity.startActivity(intent)
    }
  }
}
