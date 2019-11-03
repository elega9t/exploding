package com.elega9t.interpreter.template

import cats.data.StateT
import cats.effect.concurrent.Ref
import cats.effect.{CancelToken, IO}
import com.elega9t.interpreter.utils.CollectExceptions

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Promise}

trait StateInterpreterTemplate {

  type Data
  type Command
  type Program[A] = StateT[IO, ProcessData, A]

  case class ProcessData(data: Data)

  def applyCommand(command: Command): Program[Unit]

  protected def write(f: Data => Data): Program[Unit] =
    StateT.modify(processData => processData.copy(data = f(processData.data)))

  protected def read[A](f: Data => A): Program[A] =
    StateT.inspect(processData => f(processData.data))

  def run[A](data: Data)(program: Program[A]): (ProcessData, Either[Throwable, A]) = {
    // if the processor includes any async part, we should cancel them if the main thread is interrupted
    val promise = Promise[(ProcessData, Either[Throwable, A])]()

    val cancel: CancelToken[IO] = Ref[IO]
      .of(ProcessData(data))
      .flatMap { dataRef =>
        dataRef.get.flatMap(program.run)
          .runCancelable {
            case Left(error) =>
              dataRef.get.map((_, Left(error)))
            case Right((updatedData, result)) =>
              IO(promise.trySuccess((updatedData, Right(result))))
          }
          .toIO
      }
      .unsafeRunSync()

    try {
      Await.result(promise.future, Duration.Inf)
    } catch {
      case t: InterruptedException =>
        throw CollectExceptions()
          .run(cancel.unsafeRunSync())
          .run(throw t)
          .get
          .get
    }

  }

  object Interpreter {

    def applyCommand(command: Command): Program[Unit] =
      StateInterpreterTemplate.this.applyCommand(command)

    def pure[A](value: A): Program[A] =
      StateT.pure(value)

    val unit: Program[Unit] = pure(())

    def read[A](f: Data => A): Program[A] =
      StateInterpreterTemplate.this.read(f)

  }

}
