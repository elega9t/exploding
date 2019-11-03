package com.elega9t.exploding.model

sealed trait Card extends Product with Serializable

object Card {

  case class Blank(id: String) extends Card

  case class Explosive(id: String) extends Card

}
