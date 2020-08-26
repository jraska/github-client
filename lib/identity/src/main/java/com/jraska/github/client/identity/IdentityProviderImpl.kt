package com.jraska.github.client.identity

import com.jraska.github.client.identity.internal.AnonymousIdentity

class IdentityProviderImpl internal constructor(
  private val anonymousIdentity: AnonymousIdentity
) {

  fun session(): Session {
    return everythingAnonymousNow()
  }

  private fun everythingAnonymousNow(): Session {
    return anonymousIdentity.session()
  }
}
