package com.navneetgupta.bonify.play.controllers

import play.api.mvc._
import play.api.libs.json.Json._
import scala.concurrent.ExecutionContext
import com.navneetgupta.bonify.play.command.UrlReq
import com.navneetgupta.bonify.play.service.BonifyServices
import com.navneetgupta.bonify.play.model._
import akka.actor.ActorSystem
import com.navneetgupta.bonify.play.actor.UrlParserActor
import akka.util.Timeout
import com.navneetgupta.bonify.play.command.Greeting
import com.navneetgupta.bonify.play.actor.PathKeyGeneratorActor
import com.navneetgupta.bonify.play.command.GetPathParamsKey
import scala.concurrent.Future
import com.navneetgupta.bonify.play.command.GetPathParamMap
import com.navneetgupta.bonify.play.command.BonifyResponse
import com.navneetgupta.bonify.play.model.ResponseModel._

class BonifySystemController(cc: ControllerComponents, api: BonifyServices, system: ActorSystem)(implicit ec: ExecutionContext) extends AbstractController(cc){
  import scala.concurrent.duration._
  import akka.pattern.ask
  implicit val timeout: Timeout = 5.seconds
  lazy val urlParserActor = system.actorOf(UrlParserActor.props,"url-parser")
  lazy val pathParamKeyGenerator = system.actorOf(PathKeyGeneratorActor.props,"path-key-generator")
  
  def greetings = Action.async {
    val r = (urlParserActor ? Greeting).mapTo[ServiceResult[String]]
    r.map {
      case s:FullResult[String] => Ok("Got Success")
      case _ => Ok("Faliure")
    }
  }
  def parseUrl(): Action[UrlReq] = Action.async(parse.json[UrlReq]) { implicit req =>
    val m = req.body
    /*
     * The Below call to Get the Keys List (pathParamKeyGenerator ? GetPathParamsKey(m.originalReq)) Could be Minimized.
     * Since its a Constatnt part whihc could be generated while Compilation or the Initialization part .
     * 
     * Here Just to Verify the different Request two Seperate calls has been made.
     * 
     * Considering the PathKey List is Generated  at Compile time or Initialization time
     * 
     * Only Process required during each call is Parsing of Actual URL. 
     * Which in this case is taking a O(n) time where n is the length of the URL
     * */
    val r = (pathParamKeyGenerator ? GetPathParamsKey(m.originalReq)).mapTo[ServiceResult[List[Option[String]]]]
    r.flatMap {
      case f: FullResult[List[Option[String]]] =>
        println(f.getOrElse(Nil))
        val r2 = (urlParserActor ? GetPathParamMap(f.getOrElse(Nil),m.url)).mapTo[ServiceResult[Map[String,String]]]
        r2.map {
          case f: FullResult[Map[String,String]] => success(data = Some(toJson(BonifyResponse(f.getOrElse(Map.empty)))))
          case EmptyResult => Ok("Parsing Failed")
          case Failure(_, _, _) => Ok("Parsing Failed")
        }
         
      case EmptyResult => Future{Ok("Parsing Failed")}
      case Failure(_, _, _) => Future{Ok("Parsing Failed")}
    }
  }
}