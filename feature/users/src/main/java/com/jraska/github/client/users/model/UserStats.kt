package com.jraska.github.client.users.model

import org.threeten.bp.Instant

internal class UserStats(val followers: Int, val following: Int, val publicRepos: Int, val joined: Instant)
