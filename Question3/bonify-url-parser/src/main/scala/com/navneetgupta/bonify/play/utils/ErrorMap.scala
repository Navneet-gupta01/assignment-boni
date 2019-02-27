package com.navneetgupta.bonify.play.utils

object ErrorCodesMap {

  val errorMap = Map(200 -> "SUCCESS",
                     400 -> "Invlaid Input Data or Bad Request",
                     401 -> "User is  Not Authorized for this Resource",
                     404 -> "Requested Resource Not found",
                     408 -> "Connection Timed Out",
                     500 -> "System Failure",
                     502 -> "Invlaid Output Format",
                     503 -> "Unable to add the Requested Item")
   val errorReasonMap = Map(200 -> "OK",
                            400 -> "BAD_REQUEST",
                            401 -> "UN_AUTHORIZED",
                            404 -> "NOT_FOUND",
                            408 -> "REQUEST_TIMEOUT",
                            500 -> "INTERNAL_SERVER_ERROR",
                            502 -> "BAD_GATEWAY",
                            503 -> "SERVICE_UNAVAILABLE")
   
   def getErrorMap(): Map[Int,String] = {errorMap}
   def getErrorReasonMap() : Map[Int,String] = {errorReasonMap}
}