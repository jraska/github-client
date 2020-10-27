package com.jraska.github.client.config.debug.ui

interface ConfigRowModelProvider {

  // Casting will be needed, but not referencing Epoxy here to keep Android out of the api module
  fun epoxyModels(): List<Any>
}
