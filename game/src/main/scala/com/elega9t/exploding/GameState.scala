package com.elega9t.exploding

import java.util.UUID

import com.elega9t.exploding.dsl._
import com.elega9t.exploding.model.Card
import com.elega9t.exploding.dsl.Command._

object GameState extends State[GameInterpreter.Program] {

  override implicit val Uuid: Uuid[GameInterpreter.Program] = new Uuid[GameInterpreter.Program] {
    override def next(): GameInterpreter.Program[UUID] =
      GameInterpreter.RandomState.randomArray(16).map(RandomUuid.fromBytes)
  }

  override implicit val Shuffle: Shuffle[GameInterpreter.Program] = new Shuffle[GameInterpreter.Program] {
    override def apply(deck: Vector[Card]): GameInterpreter.Program[Vector[Card]] =
      GameInterpreter.RandomState.shuffle(deck)
  }

  override implicit val Deck: Deck[GameInterpreter.Program] = new DeckLocal[GameInterpreter.Program] {
    override protected def insert(value: Card): GameInterpreter.Program[Unit] =
      GameInterpreter.applyCommand(DeckCardInsert(value))

    override protected def delete(value: Card): GameInterpreter.Program[Unit] =
      GameInterpreter.applyCommand(DeckCardDelete(value))

    override def all(): GameInterpreter.Program[Vector[Card]] =
      GameInterpreter.Interpreter.read(_.deck)
  }

  override implicit val User: User[GameInterpreter.Program] = new UserLocal[GameInterpreter.Program] {
    override protected def insert(value: Card.Diffuse): GameInterpreter.Program[Unit] =
      GameInterpreter.applyCommand(UserCardInsert(value))

    override def delete(value: Card.Diffuse): GameInterpreter.Program[Unit] =
      GameInterpreter.applyCommand(UserCardDelete(value))

    override def all(): GameInterpreter.Program[Vector[Card.Diffuse]] =
      GameInterpreter.Interpreter.read(_.user)
  }

}
