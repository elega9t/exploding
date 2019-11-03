package com.elega9t.exploding

import com.elega9t.exploding.dsl.Command.{CardDelete, CardInsert, RandomNumbers}
import com.elega9t.interpreter.template.{RandomSupport, StateInterpreterTemplate, StateProcessTemplate}

case object GameInterpreter extends StateInterpreterTemplate with StateProcessTemplate with RandomSupport {

  override type Data = dsl.Data
  override type Command = dsl.Command

  override def getRandomData(data: Data): RandomSupport.Data =
    data.randomData

  override def setRandomData(data: Data, randomData: RandomSupport.Data): Data =
    data.copy(randomData = randomData)

  override def liftRandomCommand(command: RandomSupport.Command): Command =
    RandomNumbers(command)

  override def applyCommand(command: Command): Program[Unit] = command match {

    case CardInsert(value) => write(data => data.copy(cards = data.cards :+ value))

    case CardDelete(value) => write(data => data.copy(cards = data.cards.filterNot(_ == value)))

    case RandomNumbers(cmd) => applyRandomCommand(cmd)

  }

}
