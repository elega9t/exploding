import sbt.Keys._

lazy val interpreter = project.in(file("interpreter"))

lazy val gameModel = project.in(file("model"))

lazy val gameDsl = project.in(file("dsl"))
  .dependsOn(interpreter, gameModel)

lazy val gameLogic = project.in(file("logic"))
  .dependsOn(gameDsl)

lazy val game = project.in(file("game"))
    .dependsOn(gameLogic)

name := "exploding"

Common.settings