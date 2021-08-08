package com.jraska.github.client

/**
 * This enum is a demonstration how to scale maintenance of keys like analytics and remote configuration over the long term.
 *
 * - All keys, which are interacting with external system have to have an owner.
 * - The reason for that is the input for these is dynamic, by time it is not clear if they are still used or consumed and tracing them is difficult.
 *
 * - Each type of a resource coming from outside has also a stale state after some time of inactivity during keys audit.
 * - In case of some key being stale - it can be deleted together with related code.
 * - At the moment some key has an Owner#Unknown - it becomes stale.
 *
 * @see AnalyticsEvent.Key
 * @see Config.Key for remote configuration
 */
enum class Owner {
  CORE_TEAM,
  USERS_TEAM,
  UNKNOWN_STALE // Stale! Can be deleted at any moment during resource key audit.
}
