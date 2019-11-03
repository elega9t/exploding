package com.elega9t.exploding.dsl

import com.elega9t.exploding.model.Card
import com.elega9t.interpreter.template.RandomSupport

sealed trait Command extends Product with Serializable

object Command {

  sealed trait DeckCardUpsert extends Command {
    def value: Card
  }
  case class DeckCardInsert(value: Card) extends DeckCardUpsert
  case class DeckCardDelete(value: Card) extends Command

  sealed trait UserCardUpsert extends Command {
    def value: Card
  }
  case class UserCardInsert(value: Card.Diffuse) extends DeckCardUpsert
  case class UserCardDelete(value: Card.Diffuse) extends Command

  case class RandomNumbers(command: RandomSupport.Command) extends Command

}
