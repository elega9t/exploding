package com.elega9t.exploding.logic

sealed trait GameResult extends Product with Serializable

object GameResult {

  case object DeckInitialized extends GameResult

  case object DeckIsEmpty extends GameResult

  case object CardDiscarded extends GameResult

  case object Diffused extends GameResult

  case object DiffuseRetained extends GameResult

  case object PlayerLost extends GameResult

}
