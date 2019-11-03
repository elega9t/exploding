package com.elega9t.exploding.dsl

import cats.Monad
import cats.implicits._
import cats.data.OptionT
import com.elega9t.exploding.model.Card

abstract class UserLocal[F[_]](implicit F: Monad[F]) extends User[F] {

  protected def insert(value: Card.Diffuse): F[Unit]

  override def persist(value: Card.Diffuse): F[Unit] =
    insert(value)

  override def clear(): F[Unit] =
    for {
      cards <- all()
      _ <- cards.traverse_(delete)
    } yield ()

  override def peek: F[Option[Card.Diffuse]] = {
    val op = for {
      cards <- OptionT.liftF(all())
      selectedCard <- OptionT(F.pure(cards.headOption))
    } yield selectedCard

    op.value
  }

}
