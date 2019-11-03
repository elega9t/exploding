package com.elega9t.exploding

import com.elega9t.exploding.logic.GameResult
import com.elega9t.exploding.logic.GameEvent.InitializeDeck

import org.scalatest.FreeSpec

class GameInitializationTest extends FreeSpec with StateInterpreterIntegrationTestSupport {

  "should initialize game state correctly" - {

    "when InitializeDeck" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(5, 10, 6), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 5, blankCardCount = 10, diffuseCardCount = 6)
      }
    }

    "when InitializeDeck with 0 explosive cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(0, 10, 6), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 10, diffuseCardCount = 6)
      }
    }

    "when InitializeDeck with 0 blank cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(7, 0, 4), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 7, blankCardCount = 0, diffuseCardCount = 4)
      }
    }

    "when InitializeDeck with 0 diffuse cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(7, 1, 0), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 7, blankCardCount = 1, diffuseCardCount = 0)
      }
    }

    "when InitializeDeck with 0 explosive and 0 blank cards and 0 diffuse cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(0, 0, 0), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0)
      }
    }

    "when InitializeDeck with -5 explosive cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(-5, 1, 2), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 0, blankCardCount = 1, diffuseCardCount = 2)
      }
    }

    "when InitializeDeck with -9 blank cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(3, -9, 8), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 3, blankCardCount = 0, diffuseCardCount = 8)
      }
    }

    "when InitializeDeck with -4 diffuse cards" in {
      run {
        assertDeck(explosiveCardCount = 0, blankCardCount = 0, diffuseCardCount = 0) ~~>
          processEvent(InitializeDeck(3, 9, -4), expectedOutput = Some(GameResult.DeckInitialized)) ~~>
          assertDeck(explosiveCardCount = 3, blankCardCount = 9, diffuseCardCount = 0)
      }
    }

  }

}
