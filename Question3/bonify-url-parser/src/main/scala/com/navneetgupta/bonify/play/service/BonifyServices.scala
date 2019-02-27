package com.navneetgupta.bonify.play.service

import com.navneetgupta.bonify.play.command.UrlReq
import scala.concurrent.Future
import com.navneetgupta.bonify.play.model._

trait BonifyServices {
  def parseURL(command: UrlReq): Future[ServiceResult[Option[Map[String,String]]]]
}