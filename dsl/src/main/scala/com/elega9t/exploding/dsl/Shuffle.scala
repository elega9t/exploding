package com.elega9t.exploding.dsl

import com.elega9t.exploding.model.Card

trait Shuffle[F[_]] {

  def apply(deck: Vector[Card]): F[Vector[Card]]

}
