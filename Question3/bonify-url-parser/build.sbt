import Dependencies._
import sbt.Keys.libraryDependencies

name := "bonify-url-parser"

organization := "com.navneetgupta"

scalaVersion := "2.12.6"

version      := "0.1.0-SNAPSHOT"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val akkaVersion = "2.5.11"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "org.scalacheck" %% "scalacheck" % "1.13.4" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0-M3" % Test,
    "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
  )
}

fork in Test := true

scalacOptions ++= Seq("-feature", "-language:higherKinds", "-language:implicitConversions", "-deprecation",  "-Ydelambdafy:method", "-target:jvm-1.8")

javaOptions in Test ++= Seq("-Xms30m", "-Xmx30m")

//lazy val root = (project in file(".")).enablePlugins(PlayScala)
enablePlugins( PlayScala)
disablePlugins(PlayLayoutPlugin)