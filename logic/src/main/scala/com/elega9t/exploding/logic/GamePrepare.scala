package com.elega9t.exploding.logic

import cats.Monad
import com.elega9t.interpreter.Random
import com.elega9t.exploding.logic.processor._

class GamePrepare[F[_]](implicit F: Monad[F], Random: Random[F]) extends (GameEvent => F[Unit]) {

  lazy val CommonPrepare = new CommonPrepare[F]()

  def apply(event: GameEvent): F[Unit] =
    CommonPrepare(event)

}
