package com.jraska.github.client.navigation

interface Navigator {
  fun startUserDetail(login: String)

  fun startRepoDetail(fullPath: String)

  fun showSettings()

  fun showAbout()

  fun startConsole()
}
