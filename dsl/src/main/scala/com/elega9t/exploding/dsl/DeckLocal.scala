package com.elega9t.exploding.dsl

import cats.Monad
import cats.implicits._
import cats.data.OptionT
import com.elega9t.exploding.model.Card

abstract class DeckLocal[F[_]](implicit F: Monad[F], Shuffle: Shuffle[F]) extends Deck[F] {

  protected def insert(value: Card): F[Unit]
  protected def delete(value: Card): F[Unit]

  override def persist(value: Card): F[Unit] =
    insert(value)

  override def shuffle: F[Unit] =
    for {
      cards <- all()
      _ <- clear()
      shuffled <- Shuffle(cards)
      _ <- shuffled.traverse_(persist)
    } yield ()

  override def clear(): F[Unit] =
    for {
      cards <- all()
      _ <- cards.traverse_(delete)
    } yield ()

  override def next: F[Option[Card]] = {
    val op = for {
      cards <- OptionT.liftF(all())
      selectedCard <- OptionT.fromOption[F](cards.headOption)
      _ <- OptionT.liftF(delete(selectedCard))
    } yield selectedCard

    op.value
  }

}
