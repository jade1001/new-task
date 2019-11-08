package com.template.service

import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import com.template.dto.*
import com.template.flows.bankJP.IssuerJPToUserMoveTokensFlow
import com.template.flows.bankJP.SelfIssueJPFlow
import com.template.flows.bankPH.IssuerPHToUserMoveTokensFlow
import com.template.flows.bankPH.SelfIssuePHFlow
import com.template.service.interfaces.IIssuerJPMoveToUserJPService
import com.template.service.interfaces.IIssuerPHMoveToUserPHService
import com.template.service.interfaces.IIssuerSelfIssueJPService
import com.template.service.interfaces.IIssuerSelfIssuePHService
import com.template.states.UserJPState
import com.template.states.UserPHState
import com.template.webserver.NodeRPCConnection
import javassist.NotFoundException
import org.springframework.stereotype.Service

@Service
class IssuerSelfIssuePHService (private val rpc: NodeRPCConnection): IIssuerSelfIssuePHService {
    override fun getAll(): Any {
        val issuerFungibleStateAndRef = rpc.proxy.vaultQuery(FungibleToken::class.java).states
        return issuerFungibleStateAndRef.map { mapToSelfIssuePHDTO (it.state.data) }
    }
    override fun get(linearId: String): Any {
        val issuerFungibleStateAndRef = rpc.proxy.vaultQuery(FungibleToken::class.java).states
        val fungibleTokenState = issuerFungibleStateAndRef.find { it.state.data.tokenType.tokenIdentifier == "PHP" || it.state.data.tokenType.tokenIdentifier == "USD" }
                ?: throw NotFoundException("currency not found")
        return mapToSelfIssuePHDTO(fungibleTokenState.state.data)
    }
    override fun issuerSelfIssuePH(request: SelfIssuePHFlowDTO): SelfIssuePHDTO {
        val flowReturn = rpc.proxy.startFlowDynamic(
                SelfIssuePHFlow::class.java,
                request.amount
        )
        val flowResult = flowReturn.returnValue.get().coreTransaction.outputStates.first() as FungibleToken
        return mapToSelfIssuePHDTO(flowResult)
    }
}

@Service
class IssuerPHToUserPH(private val rpc: NodeRPCConnection) : IIssuerPHMoveToUserPHService
{
    override fun issuerPHToUserPH(request: IssuerPHToUserPHFlowDTO): IssuerPHToUserPHDTO {
        val flowReturn = rpc.proxy.startFlowDynamic(
                IssuerPHToUserMoveTokensFlow::class.java,
                request.name,
                request.amount
        )
        val flowResult = flowReturn.returnValue.get().coreTransaction.outputStates.first() as UserPHState
        return mapToIssuerPHToUserPHDTO(flowResult)
    }

}

//////////////////////////JP BANK

@Service
class IssuerSelfIssueJPService (private val rpc: NodeRPCConnection): IIssuerSelfIssueJPService {
    override fun getAll(): Any {
        val issuerFungibleStateAndRef = rpc.proxy.vaultQuery(FungibleToken::class.java).states
        return issuerFungibleStateAndRef.map { mapToSelfIssueJPDTO (it.state.data) }
    }
    override fun get(linearId: String): Any {
        val issuerFungibleStateAndRef = rpc.proxy.vaultQuery(FungibleToken::class.java).states
        val fungibleTokenState = issuerFungibleStateAndRef.find { it.state.data.tokenType.tokenIdentifier == "PHP" || it.state.data.tokenType.tokenIdentifier == "USD" }
                ?: throw NotFoundException("currency not found")
        return mapToSelfIssueJPDTO(fungibleTokenState.state.data)
    }
    override fun issuerSelfIssueJP(request: SelfIssueJPFlowDTO): SelfIssueJPDTO {
        val flowReturn = rpc.proxy.startFlowDynamic(
                SelfIssueJPFlow::class.java,
                request.amount
        )
        val flowResult = flowReturn.returnValue.get().coreTransaction.outputStates.first() as FungibleToken
        return mapToSelfIssueJPDTO(flowResult)
    }
}

@Service
class IssuerJPToUserJP(private val rpc: NodeRPCConnection) : IIssuerJPMoveToUserJPService
{
    override fun issuerJPToUserJP(request: IssuerJPToUserJPFlowDTO): IssuerJPToUserJPDTO {
        val flowReturn = rpc.proxy.startFlowDynamic(
                IssuerJPToUserMoveTokensFlow::class.java,
                request.name,
                request.amount
        )
        val flowResult = flowReturn.returnValue.get().coreTransaction.outputStates.first() as UserJPState
        return mapToIssuerJPToUserJPDTO(flowResult)
    }

}