package com.template.flows.bankJP

import Functions.FlowFunctions
import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.types.TokenType
import com.r3.corda.lib.tokens.contracts.utilities.heldBy
import com.r3.corda.lib.tokens.contracts.utilities.issuedBy
import com.r3.corda.lib.tokens.contracts.utilities.of
import com.r3.corda.lib.tokens.workflows.flows.issue.IssueTokensFlow
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction

@StartableByRPC
class SelfIssueJPFlow(private val amount: Double) : FlowFunctions() {
    @Suspendable
    override fun call(): SignedTransaction {
        if (ourIdentity == stringToParty("BankPH"))
            throw IllegalArgumentException("BankPH will not allowed to make self issue in this node")
        //return subFlow(FinalityFlow(getToken, listOf()))
        return if (getFungibleTokenJPList().isEmpty()){
            val getToken = subFlow(IssueTokensFlow(amount of TokenType("YENc", 2) issuedBy stringToParty("BankJP") heldBy stringToParty("BankJP")))
            subFlow(FinalityFlow(getToken, listOf()))
        }
        else {
            throw IllegalArgumentException("Cannot self issue again")
        }
    }
}