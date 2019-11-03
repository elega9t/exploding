import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtGit.git

import scala.util.Try

object Common {

  val settings = Seq(
    organization := "com.elega9t",
    version := "0.0.0-SNAPSHOT",
    scalaVersion := "2.12.7",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-target:jvm-1.8",
      "-unchecked",
      "-deprecation",
      "-feature",
      "-Xfatal-warnings",
      "-Ypartial-unification",
      "-Ybackend-parallelism",
      "4",
      "-language:higherKinds"
    ),
    resolvers += Resolver.sonatypeRepo("releases"),
    resolvers += Resolver.typesafeIvyRepo("releases"),
    addCompilerPlugin(Library.scalaKindProjector),
    javacOptions in ThisBuild ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:all", "-Werror"),
    javacOptions in ThisBuild ++= Seq("-XX:+UseG1GC", "-XX:+UseStringDeduplication", "-XX:+FlightRecorder", "-Xss1m")
  )

}
