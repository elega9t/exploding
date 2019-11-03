package com.elega9t.exploding

import com.elega9t.interpreter.template._
import com.elega9t.exploding.dsl.Command._

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

    case DeckCardInsert(value) => write(data => data.copy(deck = data.deck :+ value))

    case DeckCardDelete(value) => write(data => data.copy(deck = data.deck.filterNot(_ == value)))

    case UserCardInsert(value) => write(data => data.copy(user = data.user :+ value))

    case UserCardDelete(value) => write(data => data.copy(user = data.user.filterNot(_ == value)))

    case RandomNumbers(cmd) => applyRandomCommand(cmd)

  }

}
