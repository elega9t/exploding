package com.elega9t.exploding.dsl

import cats.Monad
import cats.data.OptionT
import cats.implicits._
import com.elega9t.exploding.model.Card

abstract class CardsLocal[F[_]](implicit F: Monad[F]) extends Cards[F] {

  protected def insert(value: Card): F[Unit]
  protected def delete(value: Card): F[Unit]

  override def persist(value: Card): F[Unit] =
    insert(value)

  override def clear(): F[Unit] =
    for {
      cards <- all()
      _ <- cards.traverse(delete)
    } yield ()

  override def next: F[Option[Card]] = {
    val op = for {
      cards <- OptionT.liftF(all())
      selectedCard <- OptionT(F.pure(cards.headOption))
      _ <- OptionT.liftF(delete(selectedCard))
    } yield selectedCard

    op.value
  }

}
