package com.jraska.github.client.identity

import com.jraska.github.client.PerApp
import com.jraska.github.client.identity.internal.AnonymousIdentity
import com.jraska.github.client.identity.internal.SessionIdProvider
import com.jraska.github.client.time.TimeProvider
import dagger.Module
import dagger.Provides

@Module
object IdentityModule {

  @Provides
  @PerApp
  internal fun provideSessionIdProvider(timeProvider: TimeProvider): SessionIdProvider {
    return SessionIdProvider(timeProvider)
  }

  @Provides
  @PerApp
  internal fun identityProvider(anonymousIdentity: AnonymousIdentity): IdentityProvider {
    return IdentityProvider(anonymousIdentity)
  }
}
