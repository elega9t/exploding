package com.elega9t.interpreter

trait Random[F[_]] {

  def initRandom: F[Unit]

  def randomArray(size: Int): F[Array[Byte]]

  def shuffle[V](values: Vector[V]): F[Vector[V]]

}
