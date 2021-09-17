package com.jraska.github.client.android.test

import androidx.test.espresso.IdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import java.util.Collections
import java.util.WeakHashMap
import kotlin.coroutines.CoroutineContext

class IdlingDispatcher(
  private val parent: CoroutineDispatcher
) : CoroutineDispatcher(), IdlingResource {

  private val jobs = Collections.newSetFromMap(WeakHashMap<Job, Boolean>())
  private var idlingCallback: IdlingResource.ResourceCallback? = null

  override fun getName() = "Coroutines IdlingDispatcher"

  override fun isIdleNow(): Boolean {
    jobs.removeAll { !it.isActive }
    return jobs.isEmpty()
  }

  override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
    idlingCallback = callback
  }

  override fun dispatch(context: CoroutineContext, block: Runnable) {
    context[Job]?.let { addNewJob(it) }
    parent.dispatch(context, block)
  }

  @InternalCoroutinesApi
  override fun dispatchYield(context: CoroutineContext, block: Runnable) {
    context[Job]?.let { addNewJob(it) }
    parent.dispatchYield(context, block)
  }

  override fun isDispatchNeeded(context: CoroutineContext): Boolean {
    context[Job]?.let { addNewJob(it) }
    return parent.isDispatchNeeded(context)
  }

  private fun addNewJob(job: Job) {
    job.invokeOnCompletion { onJobComplete() }
    jobs.add(job)
  }

  private fun onJobComplete() {
    if (isIdleNow) {
      idlingCallback?.onTransitionToIdle()
    }
  }
}
