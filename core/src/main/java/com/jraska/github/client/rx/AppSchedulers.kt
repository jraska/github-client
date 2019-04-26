package com.jraska.github.client.rx

import io.reactivex.Scheduler

class AppSchedulers(
  val mainThread: Scheduler,
  val io: Scheduler,
  val computation: Scheduler
)
