package com.jraska.github.client.coroutines

import kotlinx.coroutines.CoroutineDispatcher

var exchangeDispatcher: CoroutineDispatcher? = null

class AppDispatchers(
  val main: CoroutineDispatcher,
  val io: CoroutineDispatcher
)
