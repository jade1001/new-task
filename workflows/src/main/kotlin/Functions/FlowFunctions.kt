package Functions
import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import com.template.states.UserJPState
import com.template.states.UserPHState
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.identity.Party
import net.corda.core.node.services.queryBy
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import java.math.BigDecimal
import java.util.*

abstract class FlowFunctions : FlowLogic<SignedTransaction>()
{
    override val progressTracker = ProgressTracker(
            CREATING, VERIFYING, SIGNING, NOTARIZING, FINALIZING
    )

    fun stringToUniqueIdentifier(id: String): UniqueIdentifier
    {
        return UniqueIdentifier.fromString(id)
    }

    fun stringToParty(name: String): Party
    {
        return serviceHub.identityService.partiesFromName(name, false).singleOrNull()
                ?: throw IllegalArgumentException("No match found for $name")
    }

    fun verifyAndSign(transaction: TransactionBuilder): SignedTransaction
    {
        transaction.verify(serviceHub)
        return serviceHub.signInitialTransaction(transaction)
    }

    fun getUserByLinearId(id: String): StateAndRef<UserPHState>
    {
        val linearId = stringToUniqueIdentifier(id)
        val criteria = QueryCriteria.LinearStateQueryCriteria(linearId = listOf(linearId))
        return serviceHub.vaultService.queryBy<UserPHState>(criteria = criteria).states.single()
    }

    fun checkCurrency(cur: String): Boolean
    {
        return ((Currency.getInstance(cur)) !in Currency.getAvailableCurrencies())
    }

    fun getFungibleTokenByCurrency(currency: String): StateAndRef<FungibleToken>
    {
        val criteria = QueryCriteria.VaultQueryCriteria()
        return serviceHub.vaultService.queryBy<FungibleToken>(criteria = criteria).states.find {
            it.state.data.tokenType.tokenIdentifier == currency
        } ?: throw IllegalArgumentException("Unavailable currency")
    }

    fun getUserByNamePH(name: String): StateAndRef<UserPHState>
    {
        val criteria = QueryCriteria.VaultQueryCriteria()
        return serviceHub.vaultService.queryBy<UserPHState>(criteria = criteria).states.find {
            it.state.data.name == name
        } ?: throw IllegalArgumentException("Unavailable name")
    }

    fun getUserPHState(): List<StateAndRef<UserPHState>>
    {
        val criteria = QueryCriteria.VaultQueryCriteria()
        return serviceHub.vaultService.queryBy<UserPHState>(criteria = criteria).states
    }

    fun getUserJPState(): List<StateAndRef<UserJPState>>
    {
        val criteria = QueryCriteria.VaultQueryCriteria()
        return serviceHub.vaultService.queryBy<UserJPState>(criteria = criteria).states
    }

    fun getPH(name: String): Boolean {
        val criteria = QueryCriteria.VaultQueryCriteria()
        serviceHub.vaultService.queryBy<UserPHState>(criteria = criteria).states.forEach {
            if (it.state.data.name == name) return true
        }
        return false
    }

    fun getJP(name: String): Boolean {
        val criteria = QueryCriteria.VaultQueryCriteria()
        serviceHub.vaultService.queryBy<UserJPState>(criteria = criteria).states.forEach {
            if (it.state.data.name == name) return true
        }
        return false
    }

    fun getUserByNameJP(name: String): StateAndRef<UserJPState>
    {
        val criteria = QueryCriteria.VaultQueryCriteria()
        return serviceHub.vaultService.queryBy<UserJPState>(criteria = criteria).states.find {
            it.state.data.name == name
        } ?: throw IllegalArgumentException("Unavailable name")
    }

    fun getFungibleTokenPHList(): List<StateAndRef<FungibleToken>>
    {
        val criteria = QueryCriteria.VaultQueryCriteria()
        return serviceHub.vaultService.queryBy<FungibleToken>(criteria = criteria).states
    }

    fun getFungibleTokenJPList(): List<StateAndRef<FungibleToken>>
    {
        val criteria = QueryCriteria.VaultQueryCriteria()
        return serviceHub.vaultService.queryBy<FungibleToken>(criteria = criteria).states
    }

}