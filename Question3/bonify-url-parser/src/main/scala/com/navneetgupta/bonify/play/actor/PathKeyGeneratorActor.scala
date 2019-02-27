package com.navneetgupta.bonify.play.actor

import akka.actor.Props
import com.navneetgupta.bonify.play.command.GetPathParamsKey
import scala.concurrent.Future

object PathKeyGeneratorActor {
  def props =  Props[PathKeyGeneratorActor]
}
class PathKeyGeneratorActor extends BaseActor {
  import context.dispatcher
  
  override def receive = {
    case GetPathParamsKey(url) =>
      log.info("Recieved Request to Parse GetPathParamsKey : {} ", url)
      /*
       * String.split could be used But Again it would required extra processing O(2n) to remove the ** character from the keys 
       * Here processing is taking only O(n) time
       * */
      val resp = url.foldLeft((Nil:List[Option[String]],false,false,""))((a,b) => {
         b match {
           case '/' if(a._3) =>  // handle case */ that means some key is there key could be empty also, Assumption for now
             (Some(a._4)::a._1, true,false,"")
           case '/' if(a._2) => // handle case // None 
             (None::a._1,true,false,"")
           case '/' if(!a._3) =>  // handle case start of sdsdsd/  this would also consider the fist character and will insert extra None. We can discard First None in list
             (None::a._1,true,false,a._4)
           case '*' if(a._2) => // handle case /*
             (a._1,false,true,"")
           case '*' if(!a._2) => //hande case asa*
             (a._1,false,true,a._4)
           case _ if(a._3 || !a._4.isEmpty()) => //handle case where *s or ssdsd+
             (a._1,false,false,a._4+b)
           case _ => (a._1,false,false,a._4)
         }
      })
      val list:List[Option[String]] = 
        if(resp._3)
          Some(resp._4)::resp._1
        else None::resp._1
      log.info("GetPathParamsKey For URL: {} List Generated is: {}", url, list)
      pipeResponse(Future{list.reverse})
  }
}