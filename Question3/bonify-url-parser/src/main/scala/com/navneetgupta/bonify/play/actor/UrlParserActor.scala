package com.navneetgupta.bonify.play.actor

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import com.navneetgupta.bonify.play.command.UrlReq
import com.navneetgupta.bonify.play.command.Greeting
import scala.concurrent.Future
import com.navneetgupta.bonify.play.command.GetPathParamMap
import com.navneetgupta.bonify.play.model.FullResult

object UrlParserActor {
  def props = Props[UrlParserActor]
}
class UrlParserActor extends BaseActor {
  import context.dispatcher
  
  override def receive = {
    case Greeting => 
      FullResult("Navnet")
    case GetPathParamMap(pathKeyList, url) => 
      log.info("Recieved Request for GetPathParamMap for url : {} ", url)
      val resp2 = url.foldLeft((Map.empty: Map[String,String], pathKeyList, false,""))((a,b) => {
        b match {
          case '/' if(a._3 || !a._4.isEmpty()) => 
            val map = a._2.head match {
              case Some(x) => a._1 + (x -> a._4)
              case None => a._1
            }
            (map,a._2.tail,true,"")
          case '/' => (a._1,a._2.tail,true,"")
          case _ if(a._3 || !a._4.isEmpty()) => (a._1,a._2,false, a._4+b)
          case _ => a
        }
      })
      val resp = resp2._2 match {
              case Some(x1)::Nil => resp2._1 + (x1 -> resp2._4)
              case None::Nil => resp2._1
              case _ => Map()
            }
      log.info("Map Generated For Path params is  {} ", resp)
      FullResult(resp)
    case _ => 
      None
  }
}