package com.template.flows.bankPH

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
class SelfIssuePHFlow(private val amount: Double) : FlowFunctions() {
    @Suspendable
    override fun call(): SignedTransaction {
        if (ourIdentity == stringToParty("BankJP"))
            throw IllegalArgumentException("BankJP will not allowed to make self issue")

        val getToken = subFlow(IssueTokensFlow(amount of TokenType("PHPc", 2) issuedBy stringToParty("BankPH") heldBy stringToParty("BankPH")))
        return subFlow(FinalityFlow(getToken, listOf()))
    }
}