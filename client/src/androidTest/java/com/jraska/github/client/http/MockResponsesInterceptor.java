package com.jraska.github.client.http;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * We have to do this for the code lab to not exceed rate limit from one ip
 */
final class MockResponsesInterceptor implements Interceptor {

  private final Random random = new Random();
  private final MockedResponses mockedResponses = new MockedResponses();

  @Override
  public Response intercept(@NonNull Chain chain) throws IOException {
    Request request = chain.request();

    Response response = mockedResponses.get(request);
    if (response != null) {
      sleepToSimulateLatency();
      return response;
    } else {
      return chain.proceed(request);
    }
  }

  private void sleepToSimulateLatency() {
    try {
      Thread.sleep(200 + random.nextInt(600));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
