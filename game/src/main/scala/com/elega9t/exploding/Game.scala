package com.elega9t.exploding

import com.elega9t.interpreter.Process
import com.typesafe.scalalogging.StrictLogging
import com.elega9t.exploding.dsl.{Data, State}
import com.elega9t.exploding.logic.{GameOperation, GamePrepare}

object Game extends App with StrictLogging {

  import GameInterpreter._
  import GameInterpreter.StateProcess._

  implicit val State: State[F] = GameState

  val process = Process(
    new GamePrepare[F](),
    new GameOperation[F]()
  )

  val game = GameInterpreter.StateProcess(Data.empty, process)

  GameEventStream.instance().foreach { event =>
    game(event) match {
      case ProcessResult(input, _, Left(error)) =>
        logger.error(s"While executing $input", error)
      case ProcessResult(_, resultingData, Right(output)) =>
        println(output)
        println(resultingData)
    }
  }

}
