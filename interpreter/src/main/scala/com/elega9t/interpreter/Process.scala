package com.elega9t.interpreter

import cats.effect.Sync

trait Process[F[_], I, O] extends (I => (F[Unit], F[O])) { outer =>
  def apply(input: I): (F[Unit], F[O])
}

object Process {
  def apply[F[_], I, O](prepare: I => F[Unit], operation: I => F[O])(implicit F: Sync[F]): Process[F, I, O] =
    apply(Prepare(prepare), Operation(operation))

  def apply[F[_], I, O](prepare: Prepare[F, I], operation: Operation[F, I, O])(implicit F: Sync[F]): Process[F, I, O] =
    (input: I) => (F.suspend(prepare(input)), F.suspend(operation(input)))
}
