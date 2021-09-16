package com.jraska.github.client.coroutines

import kotlinx.coroutines.CoroutineDispatcher

class AppDispatchers(
  val main: CoroutineDispatcher,
  val io: CoroutineDispatcher
)
