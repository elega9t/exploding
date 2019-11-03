package com.elega9t.exploding.dsl

import java.util.UUID

import scala.util.Random

object RandomUuid extends (Random => UUID) {

  override def apply(value: Random): UUID = {
    val bytes = new Array[Byte](16)
    value.nextBytes(bytes)
    fromBytes(bytes)
  }

  def fromBytes(bytes: Array[Byte]): UUID = {
    bytes(6) = (bytes(6) & 0x0f).toByte // clear version
    bytes(6) = (bytes(6) & 0x40).toByte // set to version 4
    bytes(8) = (bytes(8) & 0x3f).toByte // clear variant
    bytes(8) = (bytes(8) & 0x80).toByte // set to IETF variant

    val msb = (0 to 7).foldLeft(0L)((r, n) => r << 8 | (bytes(n) & 0xff))
    val lsb = (8 to 15).foldLeft(0L)((r, n) => r << 8 | (bytes(n) & 0xff))

    new UUID(msb, lsb)
  }

}
