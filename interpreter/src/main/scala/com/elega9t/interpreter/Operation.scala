package com.elega9t.interpreter

import cats.data.Kleisli

object Operation {

  def apply[F[_], I, O](f: I => F[O]): Operation[F, I, O] = Kleisli(f)

}
