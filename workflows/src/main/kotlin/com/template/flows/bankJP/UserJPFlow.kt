package com.template.flows.bankJP

import Functions.FlowFunctions
import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType
import com.r3.corda.lib.tokens.contracts.types.TokenType
import com.r3.corda.lib.tokens.contracts.utilities.issuedBy
import com.r3.corda.lib.tokens.contracts.utilities.of
import com.r3.corda.lib.tokens.workflows.utilities.getPreferredNotary
import com.template.contracts.TaskContract
import com.template.states.UserJPState
import com.template.states.UserPHState
import net.corda.core.contracts.Amount
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder

@StartableByRPC
class UserJPFlow(private val name: String) : FlowFunctions(){
    @Suspendable
    override fun call(): SignedTransaction {
        if (ourIdentity == stringToParty("BankPH"))
            throw IllegalArgumentException("BankPH cannot register a new user")

        return if (getUserJPState().isEmpty()){
            subFlow(FinalityFlow(verifyAndSign(transaction()), listOf()))
        }
        else {
            if (getJP(name))
            {
                throw IllegalArgumentException("User already exists")
            }
            else{
                subFlow(FinalityFlow(verifyAndSign(transaction()), listOf()))
            }
        }
    }

    private fun outState() : UserJPState
    {
        return UserJPState(
                name = name,
                wallet = userWallet(),
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity)
        )
    }

    private fun userWallet() : MutableList<Amount<IssuedTokenType>> {
        val yen = 0 of TokenType("YEN", 2) issuedBy stringToParty("BankJP")
        val yenc = 0 of TokenType("YENc", 2) issuedBy stringToParty("BankJP")
        return mutableListOf(yen , yenc)
    }

    private fun transaction() = TransactionBuilder(notary = getPreferredNotary(serviceHub)).apply {
        val cmd = Command(TaskContract.Commands.Register(), ourIdentity.owningKey)
        addOutputState(outState(), TaskContract.ID)
        addCommand(cmd)
    }


}