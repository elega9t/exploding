package com.elega9t.interpreter.template

abstract class ProcessException(message: String, cause: Throwable) extends
  RuntimeException(message: String, cause: Throwable) with Product

abstract class FatalProcessException(message: String, cause: Throwable) extends
  ProcessException(message, cause)

abstract class NonFatalProcessException(message: String, cause: Throwable) extends
  ProcessException(message, cause)

case class PrepareException(cause: Throwable) extends
  NonFatalProcessException(cause.getMessage, cause)
case object PrepareException {
  val `type`: String = productPrefix
}

case class OperationException(cause: Throwable) extends
  NonFatalProcessException(cause.getMessage, cause)
case object OperationException {
  val `type`: String = productPrefix
}
