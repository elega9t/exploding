package com.elega9t.exploding

import com.elega9t.exploding.logic.GameResult
import com.elega9t.exploding.logic.GameEvent.InitializeDeck

import org.scalatest.FreeSpec

class GameInitializationTest extends FreeSpec with StateInterpreterIntegrationTestSupport {

  "should initialize game state correctly" - {

    "when InitializeDeck" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0) ~~>
          processEvent(InitializeDeck(5, 10), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 5, blankCardCount = 10)
      }
    }

    "when InitializeDeck with 0 explosive cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0) ~~>
          processEvent(InitializeDeck(0, 10), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 10)
      }
    }

    "when InitializeDeck with 0 blank cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0) ~~>
          processEvent(InitializeDeck(7, 0), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 7, blankCardCount = 0)
      }
    }

    "when InitializeDeck with 0 explosive and 0 blank cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0) ~~>
          processEvent(InitializeDeck(0, 0), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0)
      }
    }

    "when InitializeDeck with -5 explosive cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0) ~~>
          processEvent(InitializeDeck(-5, 0), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0)
      }
    }

    "when InitializeDeck with -9 blank cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0) ~~>
          processEvent(InitializeDeck(3, -9), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 3, blankCardCount = 0)
      }
    }

  }

}
