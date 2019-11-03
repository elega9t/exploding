package com.elega9t.exploding.dsl

import java.util.UUID

trait Uuid[F[_]] {

  def next(): F[UUID]

}
