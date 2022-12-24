package com.jraska.github.client.push

import com.jraska.github.client.core.android.TopActivityProvider
import javax.inject.Inject

internal class PressBackButtonCommand @Inject constructor(
  private val topActivityProvider: TopActivityProvider
) : PushActionCommand {
  override fun execute(action: PushAction): PushExecuteResult {
    if (topActivityProvider.topActivity == null) {
      return PushExecuteResult.SUCCESS // no-op if in background
    }

    topActivityProvider.onTopActivity {
      it.onBackPressed()
    }

    return PushExecuteResult.SUCCESS
  }
}
