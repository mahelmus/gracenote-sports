import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "gracenote-sports",
    libraryDependencies ++= Seq(
      library.kantan,
      library.kantanJava8,
      library.log4j,
      library.log4jCore,
      library.log4jSlf4j,
      library.config,
      library.pekko,
      library.pekkoStream,
      library.pekkoStreamTestKit % Test
    )
  )

lazy val library = new {
  object version {
    val log4j = "2.22.0"
    val kantan = "0.7.0"
    val config = "1.4.3"
    val pekko = "1.0.1"
  }

  val log4j = "org.apache.logging.log4j" % "log4j-api" % version.log4j
  val log4jCore = "org.apache.logging.log4j" % "log4j-core" % version.log4j
  val log4jSlf4j = "org.apache.logging.log4j" % "log4j-slf4j-impl" % version.log4j

  val kantan = "com.nrinaudo" %% "kantan.csv" % version.kantan
  val kantanJava8 = "com.nrinaudo" %% "kantan.csv-java8" % version.kantan

  val config = "com.typesafe" % "config" % version.config

  val pekko = "org.apache.pekko" %% "pekko-actor-typed" % version.pekko
  val pekkoStream = "org.apache.pekko" %% "pekko-stream" % version.pekko
  val pekkoStreamTestKit = "org.apache.pekko" %% "pekko-stream-testkit" % version.pekko

}

inThisBuild(
  List(
    scalaVersion := "2.13.12",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)
