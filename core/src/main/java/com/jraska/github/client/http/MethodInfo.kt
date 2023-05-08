package com.jraska.github.client.http

import java.lang.reflect.Type

class MethodInfo(
  val httpPath: String,
  val method: String,
  val dtoType: Type
)
