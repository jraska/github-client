package com.jraska.github.client.identity

import com.jraska.github.client.identity.internal.AnonymousIdentity
import com.jraska.github.client.identity.internal.SessionIdProvider
import com.jraska.github.client.time.TimeProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object IdentityModule {

  @Provides
  @Singleton
  internal fun provideSessionIdProvider(timeProvider: TimeProvider): SessionIdProvider {
    return SessionIdProvider(timeProvider)
  }

  @Provides
  @Singleton
  internal fun identityProvider(anonymousIdentity: AnonymousIdentity): IdentityProviderImpl {
    return IdentityProviderImpl(anonymousIdentity)
  }
}
