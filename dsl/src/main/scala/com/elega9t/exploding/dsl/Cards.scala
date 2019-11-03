package com.elega9t.exploding.dsl

import com.elega9t.exploding.model.Card

trait Cards[F[_]] {

  def persist(card: Card): F[Unit]

  def clear(): F[Unit]

  def next: F[Option[Card]]

}
