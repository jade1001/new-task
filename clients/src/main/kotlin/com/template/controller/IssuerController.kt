package com.template.controller

import com.template.dto.IssuerJPToUserJPFlowDTO
import com.template.dto.IssuerPHToUserPHFlowDTO
import com.template.dto.SelfIssueJPFlowDTO
import com.template.dto.SelfIssuePHFlowDTO
import com.template.service.interfaces.IIssuerJPMoveToUserJPService
import com.template.service.interfaces.IIssuerPHMoveToUserPHService
import com.template.service.interfaces.IIssuerSelfIssueJPService
import com.template.service.interfaces.IIssuerSelfIssuePHService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI


private const val CONTROLLER_NAME = "/api/issuer"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME)

class IssuerController (private val selfIssuePHService: IIssuerSelfIssuePHService,
                        private val moveTokensServicePH: IIssuerPHMoveToUserPHService,
                        private val selfIssueJPService: IIssuerSelfIssueJPService,
                        private val moveTokensServiceJP: IIssuerJPMoveToUserJPService) : BaseController()
{
    /*******************self issue***************************/
/**
 * self issue fungible tokens
 */
@PostMapping(value = ["/fungibleTokensPH"], produces = ["application/json"])
private fun selfIssueFungibleTokens(@RequestBody request: SelfIssuePHFlowDTO) : ResponseEntity<Any>
{
    return try {
        val response = selfIssuePHService.issuerSelfIssuePH(request)
        ResponseEntity.created(URI("/$CONTROLLER_NAME/successful")).body(response)
    } catch (e: Exception) {
        this.handleException(e)
    }
}
    /**
     * get all fungible token
     */
    @GetMapping(value = ["/fungibleTokensPH/all"], produces = ["application/json"])
    private fun getAllFungibleTokens() : ResponseEntity<Any>
    {
        return try {
            val response = selfIssuePHService.getAll()
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            this.handleException(e)
        }
    }

    /***************************move token*********************************/

    /**
     * Move to user
     */
    @PatchMapping(value = ["/movePH"], produces = ["application/json"])
    private fun issuerPHToUserPH(@RequestBody request: IssuerPHToUserPHFlowDTO) : ResponseEntity<Any>
    {
        return try {
            moveTokensServicePH.issuerPHToUserPH(request = request)
            ResponseEntity.ok("Moved Successfully")
        } catch (e: java.lang.Exception) {
            this.handleException(e)
        }
    }

    /*****************************JP BANK*********************************/


    /*******************self issue***************************/
    /**
     * self issue fungible tokens
     */
    @PostMapping(value = ["/fungibleTokensJP"], produces = ["application/json"])
    private fun selfIssueFungibleTokensJP(@RequestBody request: SelfIssueJPFlowDTO) : ResponseEntity<Any>
    {
        return try {
            val response = selfIssueJPService.issuerSelfIssueJP(request)
            ResponseEntity.created(URI("/$CONTROLLER_NAME/successful")).body(response)
        } catch (e: Exception) {
            this.handleException(e)
        }
    }
    /**
     * get all fungible token
     */
    @GetMapping(value = ["/fungibleTokensJP/all"], produces = ["application/json"])
    private fun getAllFungibleTokensJP() : ResponseEntity<Any>
    {
        return try {
            val response = selfIssueJPService.getAll()
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            this.handleException(e)
        }
    }

    /***************************move token*********************************/

    /**
     * Move to user
     */
    @PatchMapping(value = ["/moveJP"], produces = ["application/json"])
    private fun issuerJPToUserJP(@RequestBody request: IssuerJPToUserJPFlowDTO) : ResponseEntity<Any>
    {
        return try {
            moveTokensServiceJP.issuerJPToUserJP(request = request)
            ResponseEntity.ok("Moved Successfully")
        } catch (e: java.lang.Exception) {
            this.handleException(e)
        }
    }

}