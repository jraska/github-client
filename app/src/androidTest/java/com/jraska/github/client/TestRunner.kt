package com.jraska.github.client

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.runner.AndroidJUnitRunner
import com.jraska.github.client.coroutines.exchangeDispatcher
import com.squareup.rx3.idler.Rx3Idler
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import java.util.Collections
import java.util.WeakHashMap
import kotlin.coroutines.CoroutineContext

@Suppress("unused") // build.gradle
class TestRunner : AndroidJUnitRunner() {
  override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
    return super.newApplication(cl, TestUITestApp::class.java.name, context)
  }

  override fun onCreate(arguments: Bundle?) {
    super.onCreate(arguments)

    RxJavaPlugins.setInitComputationSchedulerHandler(
      Rx3Idler.create("RxJava 3.x Computation Scheduler")
    )
    RxJavaPlugins.setInitIoSchedulerHandler(
      Rx3Idler.create("RxJava 3.x IO Scheduler")
    )

    // FIXME: 17/9/21 This is dirty PoC
    val jobCheckingDispatcherWrapper = JobCheckingDispatcherWrapper(Dispatchers.IO)
    exchangeDispatcher = jobCheckingDispatcherWrapper

    val resource = object : IdlingResource {
      var callback: IdlingResource.ResourceCallback? = null

      override fun getName() = "Idling Coroutines"

      override fun isIdleNow() = !jobCheckingDispatcherWrapper.isAnyJobRunning

      override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
      }
    }
    jobCheckingDispatcherWrapper.completionEvent = {
      if(resource.isIdleNow) {
        resource.callback?.onTransitionToIdle()
      }
    }

    IdlingRegistry.getInstance().register(resource)
  }

  class JobCheckingDispatcherWrapper(private val parent: CoroutineDispatcher) :
    CoroutineDispatcher() {
    private val jobs = Collections.newSetFromMap(WeakHashMap<Job, Boolean>())

    var completionEvent: (() -> Unit)? = null

    override fun dispatch(context: CoroutineContext, block: Runnable) {
      context[Job]?.let { addNewJob(it) }
      parent.dispatch(context, block)
    }

    @InternalCoroutinesApi
    override fun dispatchYield(context: CoroutineContext, block: Runnable) {
      context[Job]?.let { addNewJob(it) }
      parent.dispatchYield(context, block)
    }

    private fun addNewJob(job: Job): Boolean {
      job.invokeOnCompletion {
        completionEvent?.invoke()
      }
      return jobs.add(job)
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
      context[Job]?.let { addNewJob(it) }
      return parent.isDispatchNeeded(context)
    }

    val isAnyJobRunning: Boolean
      get() {
        jobs.removeAll { !it.isActive }
        return jobs.isNotEmpty()
      }
  }
}
