package com.elega9t.exploding

import java.util.UUID

import com.elega9t.exploding.dsl._
import com.elega9t.exploding.model.Card
import com.elega9t.exploding.dsl.Command._

object GameState extends State[GameInterpreter.Program] {

  override implicit val Uuid: Uuid[GameInterpreter.Program] = new Uuid[GameInterpreter.Program] {
    override def next: GameInterpreter.Program[UUID] =
      GameInterpreter.RandomState.randomArray(16).map(RandomUuid.fromBytes)
  }

  override implicit val Shuffle: Shuffle[GameInterpreter.Program] = new Shuffle[GameInterpreter.Program] {
    override def apply(deck: Vector[Card]): GameInterpreter.Program[Vector[Card]] =
      GameInterpreter.RandomState.shuffle(deck)
  }

  override implicit val Cards: Cards[GameInterpreter.Program] = new CardsLocal[GameInterpreter.Program] {
    override protected def insert(value: Card): GameInterpreter.Program[Unit] =
      GameInterpreter.applyCommand(CardInsert(value))

    override protected def delete(value: Card): GameInterpreter.Program[Unit] =
      GameInterpreter.applyCommand(CardDelete(value))

    override protected def all(): GameInterpreter.Program[Vector[Card]] =
      GameInterpreter.Interpreter.read(_.cards)
  }

}
