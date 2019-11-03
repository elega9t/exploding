package com.elega9t.interpreter

import cats.data.Kleisli

object Prepare {

  def apply[F[_], I](f: I => F[Unit]): Prepare[F, I] = Kleisli(f)

}
