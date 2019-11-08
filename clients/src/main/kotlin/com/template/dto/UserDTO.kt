package com.template.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.template.states.UserJPState
import com.template.states.UserPHState

data class UserPHDTO(
        val name: String,
        val wallet:String,
        val linearId: String
)

data class UserPHFlowDTO @JsonCreator constructor(
        val name: String
)

fun mapToUserPHDTO(user: UserPHState) : UserPHDTO{
    return UserPHDTO(
            name = user.name,
            wallet = user.wallet.toString(),
            linearId = user.linearId.toString()
    )
}

////////////////////////

data class UserJPDTO(
        val name: String,
        val wallet:String,
        val linearId: String
)

data class UserJPFlowDTO @JsonCreator constructor(
        val name: String
)

fun mapToUserJPDTO(user: UserJPState) : UserJPDTO{
    return UserJPDTO(
            name = user.name,
            wallet = user.wallet.toString(),
            linearId = user.linearId.toString()
    )
}