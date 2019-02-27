addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.12")

// to format scala source code
// https://github.com/sbt/sbt-scalariform
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

// enable updating file headers eg. for copyright
// https://github.com/sbt/sbt-header
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.0.0")


// compiling *.proto files without protoc (for self contained builds)
// see: https://github.com/os72/protoc-jar
libraryDependencies ++= Seq(
  "com.github.os72" % "protoc-jar" % "3.0.0-b3"
)
