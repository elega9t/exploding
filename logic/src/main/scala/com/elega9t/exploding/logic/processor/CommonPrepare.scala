package com.elega9t.exploding.logic.processor

import cats.Monad
import cats.implicits._
import com.elega9t.interpreter.Random
import com.elega9t.exploding.logic.GameEvent

class CommonPrepare[F[_]](implicit F: Monad[F], Random: Random[F]) {

  def apply(event: GameEvent): F[Unit] =
    Random.initRandom.void

}
