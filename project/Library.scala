import sbt._

object Library {

  lazy val scalaKindProjector = "org.typelevel" %% "kind-projector" % "0.10.3"

  lazy val config = "com.typesafe" % "config" % "1.3.4"

  lazy val catsCore = "org.typelevel" %% "cats-core" % "2.0.0-M4"
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % catsCore.revision

  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

  lazy val mockito = "org.mockito" % "mockito-all" % "1.10.19"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"

}
