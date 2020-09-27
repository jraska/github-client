package com.jraska.github.client.xpush

import com.google.gson.annotations.SerializedName

class PushServerDto {
  @SerializedName("registration_ids")
  val ids = mutableListOf<String>()

  @SerializedName("data")
  val data = mutableMapOf<String, String>()
}


