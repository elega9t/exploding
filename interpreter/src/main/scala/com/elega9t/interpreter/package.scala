package com.elega9t

import cats.data.Kleisli

package object interpreter {

  type Prepare[F[_], I] = Kleisli[F, I, Unit]

  type Operation[F[_], I, O] = Kleisli[F, I, O]

}
