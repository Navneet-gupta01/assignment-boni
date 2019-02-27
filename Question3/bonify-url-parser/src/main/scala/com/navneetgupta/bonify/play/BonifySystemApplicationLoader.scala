package com.navneetgupta.bonify.play

import play.api.ApplicationLoader.Context
import play.api._
import play.api.i18n._
import play.api.routing.Router
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import com.navneetgupta.bonify.play.controllers.BonifySystemController
import com.softwaremill.macwire._

import _root_.controllers.AssetsComponents
import router.Routes
import com.navneetgupta.bonify.play.service.{BonifyServices, BonifyServiceImpl}

//import router.Routes

class BonifySystemApplicationLoader extends ApplicationLoader{
   override def load(context: ApplicationLoader.Context) = new BonifySystemcomponents(context).application
}

class BonifySystemcomponents(context: Context) extends BuiltInComponentsFromContext(context)
    with BonifySystemModule
    with AssetsComponents
    with I18nComponents with play.filters.HttpFiltersComponents {

  implicit val system = actorSystem
  lazy val timeOut = Timeout(10000, TimeUnit.MILLISECONDS)
  lazy val bonfiyServices:BonifyServices = wire[BonifyServiceImpl]
  lazy val bonifySystemController: BonifySystemController = wire[BonifySystemController]
  
  
  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }
  
  // Members declared in play.api.BuiltInComponents
  def router: play.api.routing.Router = {
    val prefix: String = "/"
    wire[Routes]
  }
  
  // Members declared in play.components.ConfigurationComponents
  def config(): com.typesafe.config.Config = ???
}