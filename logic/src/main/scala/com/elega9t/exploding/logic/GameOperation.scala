package com.elega9t.exploding.logic

import cats.Monad
import com.elega9t.exploding.dsl.State
import com.elega9t.exploding.logic.GameEvent._
import com.elega9t.exploding.logic.processor._

class GameOperation[F[_]](implicit F: Monad[F], State: State[F]) extends (GameEvent => F[GameResult]) {

  lazy val DrawCardProcessor = new DrawCardProcessor[F]()
  lazy val InitializeDeckProcessor = new InitializeDeckProcessor[F]()

  def apply(event: GameEvent): F[GameResult] = event match {
    case e: InitializeDeck =>
      InitializeDeckProcessor(e)
    case DrawCard =>
      DrawCardProcessor.apply()
  }

}
