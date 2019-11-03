package com.elega9t.exploding

import com.elega9t.exploding.logic.GameResult
import com.elega9t.exploding.logic.GameEvent.{DrawCard, InitializeDeck}

import org.scalatest.FreeSpec

class DrawCardTest extends FreeSpec with StateInterpreterIntegrationTestSupport {

  "should allow drawing cards" - {

    "when DeckIsEmpty" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0) ~~>
          processEvent(DrawCard, expectedOutput = Some(GameResult.DeckIsEmpty)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0)
      }
    }

    "when Deck has 1 blank card" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0) ~~>
          processEvent(InitializeDeck(0, 1), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 1) ~~>
          processEvent(DrawCard, expectedOutput = Some(GameResult.CardDiscarded)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0)
      }
    }

    "when Deck has 1 explosive card" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0) ~~>
          processEvent(InitializeDeck(1, 0), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 1, blankCardCount = 0) ~~>
          processEvent(DrawCard, expectedOutput = Some(GameResult.PlayerLost)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0)
      }
    }

  }

}
