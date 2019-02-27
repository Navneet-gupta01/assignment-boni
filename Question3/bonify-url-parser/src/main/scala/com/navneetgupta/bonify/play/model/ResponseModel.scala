package com.navneetgupta.bonify.play.model

import play.api.libs.json.JsObject
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.libs.json.Json
import play.api.http.Status.OK
import com.navneetgupta.bonify.play.utils.Constants._

object ResponseModel {
  def success(
    msgJson: Option[JsObject] = None,
    data: Option[JsValue] = None,
    statusCode: Int = OK): Result = {
    val resultJson = Json.obj(STATUS -> SUCCESS) ++
      msgJson.getOrElse(Json.obj(MESSAGES -> EMPTY_STRING))

    Results.Status(statusCode)(
      data match {
        case Some(x: JsValue) => resultJson ++ Json.obj(DATA -> x)
        case _ => resultJson
      })
  }
}