package com.elega9t.exploding

import java.util.Scanner

import com.elega9t.exploding.logic.GameEvent

import scala.Stream._

object GameEventStream {

  val scanner = new Scanner(System.in)

  def instance(): Stream[GameEvent] = {
    print("Command: ")
    scanner.nextLine().toLowerCase.trim match {
      case "init" =>
        GameEvent.InitializeDeck(1, 16) #:: instance()
      case "draw" =>
        GameEvent.DrawCard  #:: instance()
      case "exit" | "quit" =>
        Stream.empty
      case "" =>
        instance()
      case other =>
        println(s"Invalid command: '$other'")
        instance()
    }
  }

}
