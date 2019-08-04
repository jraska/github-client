package com.jraska.github.client.about.entrance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.jraska.github.client.core.android.BaseActivity

class AboutLaunchActivity : BaseActivity() {
  private lateinit var manager: SplitInstallManager
  private val aboutModuleName by lazy { getString(R.string.title_dynamic_feature_about) }

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(newBase)
    SplitCompat.install(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    manager = SplitInstallManagerFactory.create(this)

    if (manager.installedModules.contains(aboutModuleName)) {
      startAboutActivityByClassName()
      finish()
    } else {
      installAboutAndLaunch()
    }
  }

  private fun installAboutAndLaunch() {
    val request = SplitInstallRequest.newBuilder()
      .addModule(aboutModuleName)
      .build()

    manager.startInstall(request).addOnSuccessListener {
      startAboutActivityByClassName()
      finish()
    }
  }

  private fun startAboutActivityByClassName() {
    Intent().setClassName(packageName, "com.jraska.github.client.about.AboutActivity")
      .also {
        startActivity(it)
      }
  }
}
