package com.elega9t.exploding.logic

sealed trait GameEvent extends Product with Serializable

object GameEvent {

  case class InitializeDeck(explosiveCount: Int, blankCount: Int) extends GameEvent

  case object DrawCard extends GameEvent

}
