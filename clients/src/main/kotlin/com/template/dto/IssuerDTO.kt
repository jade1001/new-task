package com.template.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import com.template.states.UserJPState
import com.template.states.UserPHState

/**Self issue**/
data class SelfIssuePHDTO (
        val tokenType: String
)

data class SelfIssuePHFlowDTO @JsonCreator constructor (
        val amount: Double
)
fun mapToSelfIssuePHDTO (issuer: FungibleToken) : SelfIssuePHDTO {
    return SelfIssuePHDTO(
            tokenType = issuer.amount.toString()
    )
}

/**
 * Move to user
 */

data class IssuerPHToUserPHDTO(
        val name: String,
        val wallet: List<String>,
        val linearId: String
)

data class IssuerPHToUserPHFlowDTO @JsonCreator constructor(
        val name: String,
        val amount: Double
)

fun mapToIssuerPHToUserPHDTO(userState: UserPHState): IssuerPHToUserPHDTO
{
    return IssuerPHToUserPHDTO(
            name = userState.name,
            wallet = listOf(userState.wallet.toString()),
            linearId = userState.linearId.toString()
    )
}


//////////////////////////////////////////JP BANK

/**Self issue**/
data class SelfIssueJPDTO (
        val tokenType: String
)

data class SelfIssueJPFlowDTO @JsonCreator constructor (
        val amount: Double
)
fun mapToSelfIssueJPDTO (issuer: FungibleToken) : SelfIssueJPDTO {
    return SelfIssueJPDTO(
            tokenType = issuer.amount.toString()
    )
}

/**
 * Move to user
 */

data class IssuerJPToUserJPDTO(
        val name: String,
        val wallet: List<String>,
        val linearId: String
)

data class IssuerJPToUserJPFlowDTO @JsonCreator constructor(
        val name: String,
        val amount: Double
)

fun mapToIssuerJPToUserJPDTO(userState: UserJPState): IssuerJPToUserJPDTO
{
    return IssuerJPToUserJPDTO(
            name = userState.name,
            wallet = listOf(userState.wallet.toString()),
            linearId = userState.linearId.toString()
    )
}