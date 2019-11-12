package com.template.service

import com.template.common.appexceptions.NotFoundException
import com.template.dto.*
import com.template.flows.bankJP.UserJPFlow
import com.template.flows.bankPH.UserPHFlow
import com.template.service.interfaces.IUserServiceJP
import com.template.service.interfaces.IUserServicePH
import com.template.states.UserJPState
import com.template.states.UserPHState
import com.template.webserver.NodeRPCConnection
import org.springframework.stereotype.Service

@Service
class UserPHService(private val rpc: NodeRPCConnection): IUserServicePH
{
    override fun get(name: String): Any
    {
        val userStateRef = rpc.proxy.vaultQuery(UserPHState::class.java).states
        val userState = userStateRef.find { it.state.data.name == name }
                ?: throw NotFoundException("User not found")
        return mapToUserPHDTO(userState.state.data)
    }

    override fun registerUserPH(request: UserPHFlowDTO): UserPHDTO
    {
        val flowReturn = rpc.proxy.startFlowDynamic(
                UserPHFlow::class.java,
                request.name
        )
        val flowResult = flowReturn.returnValue.get().coreTransaction.outputStates.first() as UserPHState
        return mapToUserPHDTO(flowResult)
    }
}

@Service
class UserJPService(private val rpc: NodeRPCConnection): IUserServiceJP
{
    override fun get(linearId: String): Any
    {
        val userStateRef = rpc.proxy.vaultQuery(UserJPState::class.java).states
        val userState = userStateRef.find { it.state.data.linearId.toString() == linearId }
                ?: throw NotFoundException("User not found")
        return mapToUserJPDTO(userState.state.data)
    }

    override fun registerUserJP(request: UserJPFlowDTO): UserJPDTO
    {
        val flowReturn = rpc.proxy.startFlowDynamic(
                UserJPFlow::class.java,
                request.name
        )
        val flowResult = flowReturn.returnValue.get().coreTransaction.outputStates.first() as UserJPState
        return mapToUserJPDTO(flowResult)
    }
}