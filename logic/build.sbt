import sbt.Keys._

name := "logic"

Common.settings

libraryDependencies ++= Seq(
  Library.catsEffect,

  Library.mockito         % "test",
  Library.scalaTest       % "test"
)
