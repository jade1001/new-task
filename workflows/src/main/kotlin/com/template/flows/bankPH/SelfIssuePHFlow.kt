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
import org.bouncycastle.asn1.x500.style.RFC4519Style.name
import java.math.BigDecimal

@StartableByRPC
class SelfIssuePHFlow(private val amount: Double) : FlowFunctions() {
    @Suspendable
    override fun call(): SignedTransaction {
        if (ourIdentity == stringToParty("BankJP"))
            throw IllegalArgumentException("BankJP will not allowed to make self issue in this node")
        //return subFlow(FinalityFlow(getToken, listOf()))
        return if (getFungibleTokenPHList().isEmpty()){
            val getToken = subFlow(IssueTokensFlow(amount of TokenType("PHPc", 2) issuedBy stringToParty("BankPH") heldBy stringToParty("BankPH")))
            subFlow(FinalityFlow(getToken, listOf()))
        }
        else {
            throw IllegalArgumentException("Cannot self issue again")
        }
    }
}