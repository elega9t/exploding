import sbt.Keys._

name := "game"

Common.settings

libraryDependencies ++= Seq(
  Library.catsEffect,
  Library.logback,
  Library.scalaLogging,

  Library.mockito         % "test",
  Library.scalaTest       % "test"
)
