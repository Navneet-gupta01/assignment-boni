import Dependencies._

name := "bonify-ex4"

organization := "com.navneetgupta"

scalaVersion := "2.12.6"

version      := "0.1.0"

resourceDirectory in Compile := baseDirectory.value / "resources"

libraryDependencies ++= {
    	val akkaVersion = "2.5.12"
    	Seq(
    		"org.scalatest" %% "scalatest" % "3.0.1" % "test",
	    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
	    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
	    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
	    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
	    "ch.qos.logback" % "logback-classic" % "1.2.3",
	    "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "0.20",
	    "org.postgresql" % "postgresql" % "9.3-1100-jdbc41",
    	)
}
