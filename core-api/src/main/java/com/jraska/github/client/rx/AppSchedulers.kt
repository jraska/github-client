package com.jraska.github.client.rx

import io.reactivex.rxjava3.core.Scheduler

class AppSchedulers(
  val mainThread: Scheduler,
  val io: Scheduler,
  val computation: Scheduler
)
