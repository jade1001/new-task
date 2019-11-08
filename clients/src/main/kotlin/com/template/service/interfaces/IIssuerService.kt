package com.template.service.interfaces

import com.template.dto.*

interface IIssuerSelfIssuePHService : IService
{
    fun issuerSelfIssuePH(request: SelfIssuePHFlowDTO) : SelfIssuePHDTO
}

interface IIssuerPHMoveToUserPHService
{
    fun issuerPHToUserPH(request: IssuerPHToUserPHFlowDTO) : IssuerPHToUserPHDTO
}

/////

interface IIssuerSelfIssueJPService : IService
{
    fun issuerSelfIssueJP(request: SelfIssueJPFlowDTO) : SelfIssueJPDTO
}

interface IIssuerJPMoveToUserJPService
{
    fun issuerJPToUserJP(request: IssuerJPToUserJPFlowDTO) : IssuerJPToUserJPDTO
}