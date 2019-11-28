package com.jraska.github.client.users

import com.jraska.github.client.HasRetrofit
import retrofit2.Retrofit

class SimpleRetrofitHolder(private val retrofit: Retrofit) : HasRetrofit {
  override fun retrofit(): Retrofit {
    return retrofit
  }
}
