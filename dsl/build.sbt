import sbt.Keys._

name := "dsl"

Common.settings

libraryDependencies ++= Seq(
  Library.catsEffect,

  Library.mockito         % "test",
  Library.scalaTest       % "test"
)
