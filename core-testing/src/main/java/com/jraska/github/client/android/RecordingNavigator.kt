package com.jraska.github.client.android

import com.jraska.github.client.navigation.Navigator
import okhttp3.HttpUrl

class RecordingNavigator: Navigator {
  val screensStarted: MutableList<Any> = mutableListOf()

  override fun launchOnWeb(httpUrl: HttpUrl) {
    screensStarted.add(httpUrl)
  }

  override fun startUserDetail(login: String) {
    screensStarted.add(login)
  }

  override fun startRepoDetail(fullPath: String) {
    screensStarted.add(fullPath)
  }

  override fun showSettings() {
    screensStarted.add("settings")
  }

  override fun showAbout() {
    screensStarted.add("about")
  }

  override fun startConsole() {
    screensStarted.add("console")
  }
}
