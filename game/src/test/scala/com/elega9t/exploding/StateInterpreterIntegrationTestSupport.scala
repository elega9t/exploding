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

  def debug: F[Unit] = {
    for {
      deck <- State.Deck.all()
      user <- State.User.all()
    } yield {
      println(s"Deck: $deck")
      println(s"User: $user")
    }
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

  def assertDeck(explosiveCardCount: Int, blankCardCount: Int, diffuseCardCount: Int): F[Unit]  =
    for {
      cards <- State.Deck.all()
    } yield {
      val byType = cards.groupBy(_.productPrefix)
      byType.get(Card.Explosive.productPrefix).map(_.size) shouldBe Some(explosiveCardCount).filter(_ > 0)
      byType.get(Card.Blank.productPrefix).map(_.size) shouldBe Some(blankCardCount).filter(_ > 0)
      byType.get(Card.Diffuse.productPrefix).map(_.size) shouldBe Some(diffuseCardCount).filter(_ > 0)
    }

  def assertUser(cardCount: Int): F[Unit]  =
    for {
      cards <- State.User.all()
    } yield {
      cards.size shouldBe cardCount
    }

}
