package com.elega9t.exploding

import com.elega9t.exploding.logic.GameResult
import com.elega9t.exploding.logic.GameEvent.{DrawCard, InitializeDeck}

import org.scalatest.FreeSpec

class DrawCardTest extends FreeSpec with StateInterpreterIntegrationTestSupport {

  "should allow drawing cards" - {

    "when DeckIsEmpty" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(DrawCard, expectedOutput = Some(GameResult.DeckIsEmpty)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0)
          assertUser(cardCount = 0)
      }
    }

    "when Deck has 1 blank card" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(0, 1, 0), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 1, diffuseCardCount = 0) ~~>
          assertUser(cardCount = 1) ~~>
          processEvent(DrawCard, expectedOutput = Some(GameResult.CardDiscarded)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          debug ~~>
          assertUser(cardCount = 1)
      }
    }

    "when Deck has 1 explosive card" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(1, 0, 0), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 1, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(DrawCard, expectedOutput = Some(GameResult.Diffused)) ~~>
          assertDeck(explosiveCardCount = 1, blankCardCount = 0, diffuseCardCount = 0) ~~>
          assertUser(cardCount = 0)
      }
    }

    "when Deck has 1 diffuse card" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(0, 0, 1), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 1) ~~>
          processEvent(DrawCard, expectedOutput = Some(GameResult.DiffuseRetained)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          assertUser(cardCount = 2)
      }
    }

    "when all cards are drawn" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(0, 3, 0), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 3, diffuseCardCount = 0) ~~>
          processEvent(DrawCard, expectedOutput = Some(GameResult.CardDiscarded)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 2, diffuseCardCount = 0) ~~>
          processEvent(DrawCard, expectedOutput = Some(GameResult.CardDiscarded)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 1, diffuseCardCount = 0) ~~>
          processEvent(DrawCard, expectedOutput = Some(GameResult.CardDiscarded)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(DrawCard, expectedOutput = Some(GameResult.DeckIsEmpty)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0)
      }
    }

  }

}
