package com.template.flows.bankPH

import Functions.FlowFunctions
import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType
import com.r3.corda.lib.tokens.contracts.types.TokenType
import com.r3.corda.lib.tokens.contracts.utilities.issuedBy
import com.r3.corda.lib.tokens.contracts.utilities.of
import com.r3.corda.lib.tokens.workflows.utilities.getPreferredNotary
import com.template.contracts.TaskContract
import com.template.states.UserPHState
import net.corda.core.contracts.Amount
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder

@StartableByRPC
class UserPHFlow(private val name: String) : FlowFunctions(){
    @Suspendable
    override fun call(): SignedTransaction {
        if (ourIdentity == stringToParty("BankJP"))
            throw IllegalArgumentException("BankJP cannot register a new user")

        return if (getUserPHState().isEmpty()){
            subFlow(FinalityFlow(verifyAndSign(transaction()), listOf()))
        }
        else {
            if (getPH(name))
            {
                throw IllegalArgumentException("User already exists")
            }
            else{
                subFlow(FinalityFlow(verifyAndSign(transaction()), listOf()))
            }
        }
    }

    private fun outState() : UserPHState
    {
        return UserPHState(
                name = name,
                wallet = userWallet(),
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity)
        )
    }

    private fun userWallet() : MutableList<Amount<IssuedTokenType>> {
        val php = 0 of TokenType("PHP", 2) issuedBy stringToParty("BankPH")
        val phpc = 0 of TokenType("PHPc", 2) issuedBy stringToParty("BankPH")
        return mutableListOf(php , phpc)
    }

    private fun transaction() = TransactionBuilder(notary = getPreferredNotary(serviceHub)).apply {
        val cmd = Command(TaskContract.Commands.Register(), ourIdentity.owningKey)
        addOutputState(outState(), TaskContract.ID)
        addCommand(cmd)
    }


}