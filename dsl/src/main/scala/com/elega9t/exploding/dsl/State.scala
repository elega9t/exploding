package com.elega9t.exploding.dsl

trait State[F[_]] {

  implicit val Uuid: Uuid[F]

  implicit val Shuffle: Shuffle[F]

  implicit val Cards: Cards[F]

}
