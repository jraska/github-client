package com.jraska.github.client.firebase.report

import com.jraska.github.client.firebase.Device
import com.jraska.github.client.firebase.TestOutcome

class DeviceRunOutcome(
  val device: Device,
  val outcome: TestOutcome
)
