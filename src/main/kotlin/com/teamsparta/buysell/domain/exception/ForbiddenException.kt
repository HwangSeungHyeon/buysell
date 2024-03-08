package com.teamsparta.buysell.domain.exception

data class ForbiddenException(
    override val message: String
) : RuntimeException(message)

