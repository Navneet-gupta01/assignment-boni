package com.navneetgupta.bonify.play.command

import play.api.libs.json._

case class UrlReq(url: String, originalReq: String)

object UrlReq {
  implicit lazy val urlReqFormat: OFormat[UrlReq] =
    Json.format[UrlReq]
}
 object Greeting
 
case class GetPathParamsKey(originalUrl: String)
object GetPathParamsKey {
   implicit lazy val getPathParamsKey: OFormat[GetPathParamsKey] =
    Json.format[GetPathParamsKey]
}
 
case class GetPathParamMap(paramKeyList: List[Option[String]], url: String)

object GetPathParamMap {
  implicit def optionFormat[T: Format]: Format[Option[T]] = new Format[Option[T]]{
    override def reads(json: JsValue): JsResult[Option[T]] = json.validateOpt[T]

    override def writes(o: Option[T]): JsValue = o match {
      case Some(t) ⇒ implicitly[Writes[T]].writes(t)
      case None ⇒ JsNull
    }
  }
  implicit lazy val getPathParamMapFormat: OFormat[GetPathParamMap] =
    Json.format[GetPathParamMap]
}

case class BonifyResponse(map: Map[String,String]) 

object BonifyResponse {
  implicit lazy val bonifyResponse: OFormat[BonifyResponse] =
    Json.format[BonifyResponse]
}