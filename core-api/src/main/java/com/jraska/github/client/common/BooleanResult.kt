package com.jraska.github.client.common

/**
 * More explicit replacement for boolean return type, representing success of execution
 */
enum class BooleanResult {
  SUCCESS,
  FAILURE
}

fun BooleanResult.toBoolean(): Boolean {
  return this == BooleanResult.SUCCESS
}
