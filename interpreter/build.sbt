import sbt.Keys._

name := "interpreter"

Common.settings

libraryDependencies ++= Seq(
  Library.catsEffect,

  Library.mockito         % "test",
  Library.scalaTest       % "test"
)
