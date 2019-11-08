package com.template.flows.bankJP

import Functions.FlowFunctions
import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType
import com.r3.corda.lib.tokens.contracts.types.TokenType
import com.r3.corda.lib.tokens.contracts.utilities.issuedBy
import com.r3.corda.lib.tokens.contracts.utilities.of
import com.r3.corda.lib.tokens.workflows.flows.rpc.RedeemFungibleTokens
import com.r3.corda.lib.tokens.workflows.utilities.getPreferredNotary
import com.template.contracts.TaskContract
import com.template.states.UserJPState
import com.template.states.UserPHState
import net.corda.core.contracts.Amount
import net.corda.core.contracts.Command
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder

@StartableByRPC
@InitiatingFlow
class IssuerJPToUserMoveTokensFlow(private val name: String,
                                   private val amount: Double): FlowFunctions() {
    @Suspendable
    override fun call(): SignedTransaction {
        if (ourIdentity == stringToParty("BankPH"))
            throw IllegalArgumentException("BankPH cannot move tokens to JP users")


        val fungibleToken = getFungibleTokenByCurrency("YENc").state.data
        val redeemedToken = amount of TokenType("YENc", 2)

        subFlow(RedeemFungibleTokens(redeemedToken, fungibleToken.issuer))

        return subFlow(FinalityFlow(verifyAndSign(txBuilder()), listOf()))
    }

    private fun outState() : UserJPState
    {
        val temp : MutableList<Amount<IssuedTokenType>> = mutableListOf()
        val redeemedToken = amount of TokenType("YENc", 2) issuedBy stringToParty("BankJP")

        val userState = getUserByNameJP(name).state.data
        userState.wallet.forEach {
            if (it.token == redeemedToken.token)
            {
                temp.add(it.plus(redeemedToken))
            }
            else temp.add(it)
        }
        return userState.copy(wallet = temp)
    }


    private fun txBuilder() = TransactionBuilder(notary = getPreferredNotary(serviceHub)).apply {
        val cmd = Command(TaskContract.Commands.Move(), ourIdentity.owningKey)
        addInputState(getUserByNameJP(name))
        addOutputState(outState(), TaskContract.ID)
        addCommand(cmd)

    }
}