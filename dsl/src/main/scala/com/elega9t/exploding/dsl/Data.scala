package com.elega9t.exploding.dsl

import com.elega9t.exploding.model.Card
import com.elega9t.interpreter.template.RandomSupport

case class Data(randomData: RandomSupport.Data,
                cards: Vector[Card])

object Data {

  def empty: Data = Data(
    randomData = RandomSupport.Data(),
    cards = Vector.empty
  )

}
