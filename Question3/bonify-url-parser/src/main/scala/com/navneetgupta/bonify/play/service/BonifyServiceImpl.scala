package com.navneetgupta.bonify.play.service

import com.navneetgupta.bonify.play.command.UrlReq
import scala.concurrent.Future
import com.navneetgupta.bonify.play.model.ServiceResult

class BonifyServiceImpl(
    timeout: _root_.akka.util.Timeout)
    extends BonifyServices {
   private implicit val askTimeout = timeout
   
   override def parseURL(command: UrlReq): Future[ServiceResult[Option[Map[String,String]]]] = ???
}