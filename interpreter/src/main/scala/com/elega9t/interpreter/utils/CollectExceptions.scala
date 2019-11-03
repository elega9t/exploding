package com.elega9t.interpreter.utils

import scala.util.control.NonFatal

object CollectExceptions {

  val empty: CollectExceptions = new CollectExceptions(catchNonFatalOnly = false)

  def apply(exceptions: Vector[Throwable] = Vector.empty): CollectExceptions = exceptions.foldLeft(empty)(_.add(_))

  def apply(exception: Throwable, exceptions: Throwable*): NonEmptyCollectExceptions =
    NonEmptyCollectExceptions(exception, exceptions: _*)

}

sealed class CollectExceptions protected (val catchNonFatalOnly: Boolean) {

  def get: Option[Throwable] = None

  def withCatchNonFatalOnly(value: Boolean): CollectExceptions = new CollectExceptions(value)

  def add(exception: Throwable, moreExceptions: Throwable*): NonEmptyCollectExceptions =
    NonEmptyCollectExceptions(exception, moreExceptions: _*).withCatchNonFatalOnly(catchNonFatalOnly)

  def run(f: => Unit): CollectExceptions =
    try {
      f
      this
    } catch {
      case ex: Throwable if NonFatal(ex) || !catchNonFatalOnly =>
        add(ex)
    }

  def map(f: Throwable => Throwable): CollectExceptions = this

  def foreach(f: Throwable => Unit): Unit = get.foreach(f)

  def tap(f: Throwable => Unit): this.type = {
    foreach(f)
    this
  }

  def reThrow(): Unit = ()

}

object NonEmptyCollectExceptions {

  def apply(exception: Throwable, exceptions: Throwable*): NonEmptyCollectExceptions =
    exceptions.foldLeft(new NonEmptyCollectExceptions(exception, false))(_.add(_))

}

final class NonEmptyCollectExceptions private (val value: Throwable, catchNonFatalOnly: Boolean) extends CollectExceptions(catchNonFatalOnly) {

  override def get: Option[Throwable] = Some(value)

  override def withCatchNonFatalOnly(value: Boolean): NonEmptyCollectExceptions = new NonEmptyCollectExceptions(this.value, value)

  override def add(exception: Throwable, moreExceptions: Throwable*): NonEmptyCollectExceptions =
    new NonEmptyCollectExceptions(moreExceptions.foldLeft(prioritizeExceptions(value, exception))(prioritizeExceptions), catchNonFatalOnly)
      .withCatchNonFatalOnly(catchNonFatalOnly)

  override def run(f: => Unit): NonEmptyCollectExceptions =
    try {
      f
      this
    } catch {
      case ex: Throwable if NonFatal(ex) || !catchNonFatalOnly => add(ex)
    }

  override def map(f: Throwable => Throwable): CollectExceptions = new NonEmptyCollectExceptions(f(value), catchNonFatalOnly)

  override def reThrow(): Nothing = throw value

  private[this] def suppress(ex1: Throwable, ex2: Throwable): Throwable = {
    ex1.addSuppressed(ex2)
    ex1
  }

  private[this] def prioritizeExceptions(ex1: Throwable, ex2: Throwable): Throwable = (ex1, ex2) match {
    case (_: InterruptedException, _) => suppress(ex1, ex2)
    case (_, _: InterruptedException) => suppress(ex2, ex1)
    case _ if !NonFatal(ex1) => suppress(ex1, ex2)
    case _ if !NonFatal(ex2) => suppress(ex2, ex1)
    case _ => suppress(ex1, ex2)
  }

}
