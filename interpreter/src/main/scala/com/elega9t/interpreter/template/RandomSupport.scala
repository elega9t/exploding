package com.elega9t.interpreter.template

import java.security.SecureRandom

import cats.effect.IO
import cats.data.StateT
import com.elega9t.interpreter.Random

/**
 * Support for reproducible randomness based on randomSeed and sequenceNumber
 * i.e, for a given seed and sequenceNumber, Random operations would always return the same value
 */
object RandomSupport {

  case class Data(sequenceNumber: Long = 0L, randomSeed: Option[Vector[Byte]] = None, random: Option[scala.util.Random] = None)

  sealed trait Command

  case class SequenceNumber(value: Long) extends Command

  case class RandomSeed(value: Vector[Byte]) extends Command

  case object InitRandom extends Command

}

trait RandomSupport { parent: StateInterpreterTemplate =>

  type Data
  type Command
  type Program[_]

  def getRandomData(data: Data): RandomSupport.Data

  def setRandomData(data: Data, randomData: RandomSupport.Data): Data

  def liftRandomCommand(command: RandomSupport.Command): Command

  def applyRandomCommand(command: RandomSupport.Command): Program[Unit] = command match {
    case RandomSupport.RandomSeed(value) =>
      write(modify(_)(_.copy(randomSeed = Option(value), random = None)))

    case RandomSupport.SequenceNumber(value) =>
      write(modify(_)(_.copy(sequenceNumber = value, random = None)))

    case RandomSupport.InitRandom =>
      for {
        _ <- randomSeed
        seqNum <- Interpreter.read(getRandomData).map(_.sequenceNumber)
        _ <- applyRandomCommand(RandomSupport.SequenceNumber(seqNum + 1))
      } yield ()
  }

  implicit object RandomState extends Random[Program] {
    override def initRandom: Program[Unit] = applyRandomCommand(RandomSupport.InitRandom)
    override def randomArray(size: Int): Program[Array[Byte]] =
      getRandom.map { random =>
        val array = new Array[Byte](size)
        random.nextBytes(array)
        array
      }
    override def shuffle[V](values: Vector[V]): Program[Vector[V]] = getRandom.map(_.shuffle(values))
  }

  private def getRandom: Program[scala.util.Random] =
    Interpreter.read(getRandomData).flatMap { randomData =>
      randomData.random.map(Interpreter.pure).getOrElse {
        for {
          seed <- randomSeed
          rand = {
            val secureRandom = SecureRandom.getInstance("SHA1PRNG")
            secureRandom.setSeed(seed.toArray)
            secureRandom.setSeed(randomData.sequenceNumber)
            scala.util.Random.javaRandomToRandom(secureRandom)
          }
          _ <- StateT.modify[IO, ProcessData](modify(_)(_.copy(random = Option(rand))))
        } yield rand
      }
    }

  private def modify(data: Data)(f: RandomSupport.Data => RandomSupport.Data): Data =
    setRandomData(data, f(getRandomData(data)))

  private def modify(processData: ProcessData)(f: RandomSupport.Data => RandomSupport.Data): ProcessData =
    processData.copy(data = setRandomData(processData.data, f(getRandomData(processData.data))))

  private def randomSeed: Program[Vector[Byte]] =
    Interpreter.read(getRandomData).map(_.randomSeed).flatMap {
      _.map(Interpreter.pure).getOrElse {
        val seed = SecureRandom.getInstanceStrong.generateSeed(20).toVector
        applyRandomCommand(RandomSupport.RandomSeed(seed)).map(_ => seed)
      }
    }

}
