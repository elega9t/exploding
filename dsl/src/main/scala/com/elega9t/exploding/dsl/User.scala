package com.elega9t.exploding.dsl

import com.elega9t.exploding.model.Card

trait User[F[_]] {

  def all(): F[Vector[Card.Diffuse]]

  def delete(card: Card.Diffuse): F[Unit]

  def persist(card: Card.Diffuse): F[Unit]

  def clear(): F[Unit]

  def peek: F[Option[Card.Diffuse]]

}
