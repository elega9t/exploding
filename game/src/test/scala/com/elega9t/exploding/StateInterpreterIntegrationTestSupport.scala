package com.elega9t.exploding

import com.elega9t.exploding.logic._
import org.scalatest.{Matchers, Suite}
import com.elega9t.interpreter.Process
import com.elega9t.exploding.model.Card
import com.elega9t.exploding.dsl.{Data, State}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

trait StateInterpreterIntegrationTestSupport extends Matchers { self: Suite =>

  import GameInterpreter._
  import GameInterpreter.StateProcess._

  implicit val State: State[F] = GameState

  val process = Process(
    new GamePrepare[F](),
    new GameOperation[F]()
  )

  implicit class TestStep[T](state: F[T]) {
    def ~~>[U](nextState: F[U]): F[U] =
      state.flatMap(_ => nextState)
  }

  def run[T](state: F[T]): Unit = {
    implicit val executionContext: ExecutionContext = ExecutionContext.global
    Await.result(state.runA(ProcessData(Data.empty)).unsafeToFuture(), Duration.Inf)
  }

  def processEvent(event: GameEvent, expectedOutput: Option[GameResult] = None): F[GameResult] =
    for {
      result <- process(event)._2
    } yield {
      expectedOutput.foreach { actual =>
        result shouldBe actual
      }
      result
    }

  def assertDeck(explosiveCardCount: Int, blankCardCount: Int): F[Unit]  =
    for {
      cards <- State.Cards.all()
    } yield {
      val (explosiveCards, blankCards) = cards.partition(_.isInstanceOf[Card.Explosive])
      explosiveCards.size shouldBe explosiveCardCount
      blankCards.size shouldBe blankCardCount
    }

}
