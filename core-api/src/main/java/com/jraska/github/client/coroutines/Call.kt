package com.jraska.github.client.coroutines

import retrofit2.Call

fun <T> Call<T>.result(): T {
  val response = execute()
  if (response.isSuccessful) {
    return response.body()!!
  } else {
    throw retrofit2.HttpException(response)
  }
}
