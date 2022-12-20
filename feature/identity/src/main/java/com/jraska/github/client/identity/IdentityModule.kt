package com.jraska.github.client.identity

import com.jraska.github.client.identity.integrity.IntegrityCheck
import com.jraska.github.client.identity.integrity.IntegrityCheckPushCommand
import com.jraska.github.client.identity.internal.AddSessionIdInterceptor
import com.jraska.github.client.identity.internal.AnonymousIdentity
import com.jraska.github.client.identity.internal.SessionIdProvider
import com.jraska.github.client.push.PushActionCommand
import com.jraska.github.client.time.TimeProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import dagger.multibindings.StringKey
import okhttp3.Interceptor
import javax.inject.Singleton

@Module(includes = [IdentityModule.Declarations::class])
object IdentityModule {

  @Provides
  @Singleton
  internal fun provideSessionIdProvider(timeProvider: TimeProvider): SessionIdProvider {
    return SessionIdProvider(timeProvider)
  }

  @Provides
  @Singleton
  internal fun identityProviderImpl(anonymousIdentity: AnonymousIdentity): IdentityProviderImpl {
    return IdentityProviderImpl(anonymousIdentity)
  }

  @Provides
  internal fun identityProvider(impl: IdentityProviderImpl): IdentityProvider = impl

  @Provides
  @IntoSet
  internal fun addSessionIdInterceptor(identityProvider: IdentityProvider): Interceptor {
    return AddSessionIdInterceptor(identityProvider)
  }

  @Module
  abstract class Declarations {
    @Binds
    abstract fun bindIntegrityCheckTrigger(integrityCheck: IntegrityCheck): IntegrityTrigger

    @Binds
    @IntoMap
    @StringKey("integrity_check")
    abstract fun bindIntegrityCheckCommand(integrityCheckTriggerCommand: IntegrityCheckPushCommand): PushActionCommand
  }
}
