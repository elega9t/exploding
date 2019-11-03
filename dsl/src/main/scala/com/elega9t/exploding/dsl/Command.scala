package com.elega9t.exploding.dsl

import com.elega9t.exploding.model.Card
import com.elega9t.interpreter.template.RandomSupport

sealed trait Command extends Product with Serializable

object Command {

  sealed trait CardUpsert extends Command {
    def value: Card
  }
  case class CardInsert(value: Card) extends CardUpsert
  case class CardDelete(value: Card) extends Command

  case class RandomNumbers(command: RandomSupport.Command) extends Command

}
