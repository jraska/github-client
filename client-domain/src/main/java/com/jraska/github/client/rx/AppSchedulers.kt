package com.jraska.github.client.rx

import io.reactivex.Scheduler

class AppSchedulers(val mainThread: Scheduler, val io: Scheduler, val computation: Scheduler) {

  fun mainThread(): Scheduler {
    return mainThread
  }

  fun io(): Scheduler {
    return io
  }

  fun computation(): Scheduler {
    return computation
  }
}
