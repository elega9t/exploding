package com.elega9t.exploding.logic.processor

import cats.Monad
import cats.data.OptionT
import com.elega9t.exploding.dsl.State
import com.elega9t.exploding.model.Card
import com.elega9t.exploding.logic.GameResult

class DrawCardProcessor[F[_]](implicit F: Monad[F], State: State[F]) {

  import State._

  def apply(): F[GameResult] = {
    val op = for {
      nextCard <- OptionT(Cards.next)
    } yield {
      nextCard match {
        case Card.Blank(_) =>
          GameResult.CardDiscarded
        case Card.Explosive(_) =>
          GameResult.PlayerLost
      }
    }

    op.getOrElse(GameResult.DeckIsEmpty)
  }

}
