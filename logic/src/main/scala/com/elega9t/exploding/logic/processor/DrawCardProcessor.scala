package com.elega9t.exploding.logic.processor

import cats.Monad
import cats.implicits._
import cats.data.OptionT
import com.elega9t.exploding.dsl.State
import com.elega9t.exploding.model.Card
import com.elega9t.exploding.logic.GameResult

class DrawCardProcessor[F[_]](implicit F: Monad[F], State: State[F]) {

  import State._

  def apply(): F[GameResult] = {
    val op = for {
      deckNextCard <- OptionT(Deck.next)
      userNextCardOpt <- OptionT.liftF(User.peek)
      result <- processDraw(deckNextCard, userNextCardOpt)
    } yield result

    op.getOrElse(GameResult.DeckIsEmpty)
  }

  def processDraw(deckNextCard: Card, userNextCardOpt: Option[Card.Diffuse]): OptionT[F, GameResult] =
    (deckNextCard, userNextCardOpt) match {
      case (Card.Blank(_), _) =>
        OptionT.some(GameResult.CardDiscarded)
      case (Card.Explosive(_), None) =>
        OptionT.some(GameResult.PlayerLost)
      case (diffuse @ Card.Diffuse(_), _) =>
        OptionT.liftF(User.persist(diffuse).map(_ => GameResult.DiffuseRetained))
      case (explosive @ Card.Explosive(_), Some(diffuseCard @ Card.Diffuse(_))) =>
        OptionT.liftF {
          for {
            _ <- Deck.persist(explosive)
            _ <- Deck.shuffle
            _ <- User.delete(diffuseCard)
          } yield GameResult.Diffused
        }
    }

}
