package com.template.controller

import com.template.dto.UserJPFlowDTO
import com.template.dto.UserPHFlowDTO
import com.template.service.interfaces.IUserServiceJP
import com.template.service.interfaces.IUserServicePH
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

private const val CONTROLLER_NAME = "/api/users"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME)
class UserController(private val userServicePH: IUserServicePH,
                     private val userServiceJP: IUserServiceJP): BaseController()
{
    /***************************PH BANK**********************************/
/*****************************GET*****************************************/
/**
 * Get user by linear id
 */
@GetMapping(value = ["/PH/{linearId}"], produces = ["application/json"])
private fun getUserPHByLinearId(@PathVariable linearId: String): ResponseEntity<Any>
{
    return try {
        val response = userServicePH.get(linearId)
        ResponseEntity.ok(response)
    } catch (e: Exception) {
        this.handleException(e)
    }
}
/*************************************************************************/

/*****************************POST*****************************************/
/**
 * Register user
 */
@PostMapping(value = ["/PH"], produces = ["application/json"])
private fun createUserPH(@RequestBody request: UserPHFlowDTO): ResponseEntity<Any>
{
    return try {
        val response = userServicePH.registerUserPH(request)
        ResponseEntity.created(URI("/" + CONTROLLER_NAME + "/" + response.linearId)).body(response)
    } catch (e: Exception) {
        this.handleException(e)
    }
}
/**************************************************************************/

    /********************JP BANK**************************/

    /**
     * Get user by linear id
     */
    @GetMapping(value = ["/JP/{linearId}"], produces = ["application/json"])
    private fun getUserJPByLinearId(@PathVariable linearId: String): ResponseEntity<Any>
    {
        return try {
            val response = userServiceJP.get(linearId)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            this.handleException(e)
        }
    }
    /*************************************************************************/

    /*****************************POST*****************************************/
    /**
     * Register user
     */
    @PostMapping(value = ["/JP"], produces = ["application/json"])
    private fun createUserJP(@RequestBody request: UserJPFlowDTO): ResponseEntity<Any>
    {
        return try {
            val response = userServiceJP.registerUserJP(request)
            ResponseEntity.created(URI("/" + CONTROLLER_NAME + "/" + response.linearId)).body(response)
        } catch (e: Exception) {
            this.handleException(e)
        }
    }

}