package com.navneetgupta.bonify.ex4

import akka.event.LoggingAdapter
import java.nio.file.FileSystems
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContext
import akka.actor.ActorSystem
import akka.event.Logging
import scala.concurrent.Future
import akka.util.ByteString
import akka.actor.Terminated
import akka.stream.scaladsl._
import akka.stream.alpakka.slick.scaladsl._
import slick.jdbc.GetResult
import akka.Done


object CsvDbImport extends App{
  
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val log: LoggingAdapter = Logging(system.eventStream, this.getClass.getSimpleName)
  
  implicit val session = SlickSession.forConfig("slick-postgres")
  
  import session.profile.api._
  
  case class BankDetails(bankName: String, bankIdentifier: Int)
  
  val file = this.getClass.getClassLoader().
    getResource("banks.csv")

  val inPath = FileSystems.getDefault().
    getPath(file.getPath())

  val fileSource = FileIO.fromPath(inPath)
  
  val csvHandler = Flow[String].drop(1).map(_.split(",").toList)
  val sink : Sink[BankDetails, Future[Done]] = Sink.foreach[BankDetails](x => println(x.bankName))
  
  //Clear Db Of Existing Records.
  def clearDBIfExist = Slick
      .source(sql"DELETE FROM BANK_DETAILS".as[Int])
      .runWith(Sink.ignore)
    
  // Stream From File Source to DB Sink 
  def akkaStream = fileSource.
    via(Framing.delimiter(ByteString("\n"), Integer.MAX_VALUE, false)).
    map(_.utf8String).
    via(csvHandler).
    map { list =>
      BankDetails(list(0),list(1).toInt)
    }.
    runWith(
      Slick.sink(bankDetails => sqlu"""
        INSERT INTO BANK_DETAILS (id, name)
        VALUES (${bankDetails.bankIdentifier}, ${bankDetails.bankName})
        """
      )    
    )
    
  implicit val getBankResult = GetResult(r => BankDetails.apply(bankIdentifier = r.nextInt, bankName = r.nextString))
  
  //Get Details of Required ID
  def getDetail = Slick
      .source(sql"SELECT ID, NAME FROM BANK_DETAILS where id = 10040000".as[BankDetails])
      .log("bankDetails")
      .runWith(sink)

  def runExample: Future[Terminated] = (for {
    _ <- clearDBIfExist
    _ <- akkaStream
    _ <- getDetail
    term <- system.terminate()
  } yield term).recoverWith {
    case cause: Throwable =>
      log.error(cause, "Exception while executing example")
      system.terminate()
  }
  system.registerOnTermination(() => session.close())
  
  sys.addShutdownHook {
    system.terminate()
  }
  
  runExample
}
