package com.template.service.interfaces

import com.template.dto.*

interface IUserServicePH
{
    fun registerUserPH(request: UserPHFlowDTO): UserPHDTO
    fun get(name: String): Any
}

interface IUserServiceJP
{
    fun registerUserJP(request: UserJPFlowDTO): UserJPDTO
    fun get(name: String): Any
}