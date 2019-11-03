import sbt.Keys._

name := "model"

Common.settings

libraryDependencies ++= Seq(
  Library.catsEffect,

  Library.mockito         % "test",
  Library.scalaTest       % "test"
)
