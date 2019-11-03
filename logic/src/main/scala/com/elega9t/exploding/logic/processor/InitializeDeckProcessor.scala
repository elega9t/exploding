package com.elega9t.exploding.logic.processor

import cats.Monad
import cats.implicits._
import com.elega9t.exploding.dsl.State
import com.elega9t.exploding.model.Card
import com.elega9t.exploding.logic.GameResult
import com.elega9t.exploding.logic.GameEvent.InitializeDeck

class InitializeDeckProcessor[F[_]](implicit F: Monad[F], State: State[F]) {

  import State._

  def apply(event: InitializeDeck): F[GameResult] =
    for {
      _ <- Deck.clear()
      blankCards <- (1 to event.blankCount).toVector.traverse(_ => newCard(Card.Blank.apply))
      explosiveCards <- (1 to event.explosiveCount).toVector.traverse(_ => newCard(Card.Explosive.apply))
      diffuseCards <- (1 to event.diffuseCount).toVector.traverse(_ => newCard(Card.Diffuse.apply))
      shuffledDeck <- Shuffle(blankCards ++ explosiveCards ++ diffuseCards)
      _ <- shuffledDeck.traverse(Deck.persist)
      uuid <- Uuid.next()
      _ <- User.persist(Card.Diffuse(uuid.toString))
    } yield GameResult.DeckInitialized

  private def newCard(fn: String => Card): F[Card] =
    Uuid.next().map(uuid => fn(uuid.toString))

}
