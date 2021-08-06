package com.jraska.github.client.firebase

class Device(
  private val model: String,
  private val apiLevel: Int,
  private val locale: String,
  private val orientation: String
) {
  fun firebaseCommandString(): String {
    return "model=$model,version=$apiLevel,locale=$locale,orientation=$orientation"
  }

  fun cloudStoragePath(): String {
    return "$model-$apiLevel-$locale-$orientation"
  }

  companion object {
    val Pixel5 = Device("redfin", 30, "en", "portrait")
    val Pixel2 = Device("walleye", 27, "en", "portrait")
  }
}
