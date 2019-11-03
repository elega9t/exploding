package com.elega9t.interpreter.template

import cats.effect.IO
import cats.data.StateT

import com.elega9t.interpreter.Process

trait StateProcessTemplate {
  parent: StateInterpreterTemplate =>

  trait StateProcess[I, O] extends (I => ProcessResult[I, O]) {
    outer =>
    def data: Data

    def data_=(updatedData: Data): Unit

    def apply(input: I): ProcessResult[I, O]
  }

  object StateProcess {

    type F[T] = StateT[IO, ProcessData, T]

    def apply[I, O](initialData: Data, process: Process[F[*], I, O]): StateProcess[I, O] = new StateProcess[I, O] {
      var data: Data = initialData

      override def apply(input: I): ProcessResult[I, O] = {
        val previousData = data

        try {
          val (prepare, operation) = process(input)
          wrapException(run(data)(prepare))(ex => PrepareException(ex)) match {
            case (prepareData, Right(())) =>
              val (operationData, result) = wrapException(run(prepareData.data)(operation))(ex => OperationException(ex))
              result.foreach(_ => data = operationData.data)
              ProcessResult(
                input,
                operationData.data,
                result
              )
            case (prepareData, Left(failure)) =>
              ProcessResult(
                input,
                prepareData.data,
                Left(failure)
              )
          }
        } catch {
          case ex: NonFatalProcessException =>
            ProcessResult(
              input,
              previousData,
              Left(ex)
            )
        }
      }

      private def wrapException[A](value: => (ProcessData, Either[Throwable, A]))(
        handler: Throwable => NonFatalProcessException): (ProcessData, Either[NonFatalProcessException, A]) =
        value match {
          case (_, Left(e: NonFatalProcessException)) => throw e
          case (pd, Left(e)) =>
            (pd, Left(handler(e)))
          case other =>
            other.asInstanceOf[(ProcessData, Either[NonFatalProcessException, A])]
        }

    }

  }

  case class ProcessResult[+I, +O](input: I,
                                   resultingData: Data,
                                   output: Either[NonFatalProcessException, O])

}
