package com.mahelmus

import com.mahelmus.domain.{Action, ActionType, HomeOrAway, Period, Person, Team}
import com.typesafe.config.Config
import org.apache.pekko.{Done, NotUsed}
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.scaladsl.{Sink, Source}

import scala.concurrent.{ExecutionContext, Future}
import kantan.csv.{RowDecoder, _}
import kantan.csv.java8._
import kantan.csv.ops.toCsvInputOps
import org.apache.logging.log4j.{LogManager, Logger}

import java.nio.file.Path
import java.time.{Instant, LocalDate}
import java.time.format.DateTimeFormatter

trait Aggregator {
  import Aggregator._

  implicit def system: ActorSystem
  def run(dataset: String, config: Config)(implicit ec: ExecutionContext): Future[Done] ={
    val data = importData(dataset, config)
    data.wireTap { value =>
      if (value._2 % 500 == 0) {
        log.info("Processed [{}] elements from input source", value._2)
      }
    }.groupBy(Int.MaxValue, { case (action, _) => (action.person.id, action.actionType) })
      .map { case (action, _) =>
        log.info("[{}] did [{}]", action.person.name, action.actionType)

      }.mergeSubstreams.runWith(Sink.ignore)
  }
}

object Aggregator {
  private val log: Logger = LogManager.getLogger(getClass)

  case class Rest(actionId: Long,
                  competition: String,
                  matchId: Long,
                  kickoffDate: LocalDate,
                  startTime: Option[Long],
                  endTime: Option[Long],
                  shirtNum: Option[Byte],
                  personFunction: String,
                  reason: Option[String],
                  extraInfo: Option[String])

  val format = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")
  implicit val dateDecoder: CellDecoder[LocalDate] = localDateDecoder(format)
  implicit val personDecoder: RowDecoder[Person] = RowDecoder.decoder(11, 12)(Person.apply)
  implicit val subPersonDecoder: RowDecoder[Person] = RowDecoder.decoder(17, 18)(Person.apply)
  implicit val teamDecoder: RowDecoder[Team] = RowDecoder.decoder(9,10)(Team.apply)
  implicit val homeOrAwayDecoder: RowDecoder[HomeOrAway] = RowDecoder.decoder(8)(HomeOrAway.apply)
  implicit val periodDecoder: RowDecoder[Period] = RowDecoder.decoder(5)(Period.apply)
  implicit val actionTypeDecoder: RowDecoder[ActionType] = RowDecoder.decoder(4)(ActionType.apply)
  implicit val restDecoder: RowDecoder[Rest] = RowDecoder.decoder(0,1, 2, 3, 6, 7, 14, 15, 16, 17)(Rest.apply)
  implicit val actionDecoder: RowDecoder[Action] = RowDecoder.from { elems =>
    for {
      rest <- restDecoder.decode(elems)
      person <- personDecoder.decode(elems)
      subperson <- subPersonDecoder.decode(elems)
      team <- teamDecoder.decode(elems)
      homeOrAway <- homeOrAwayDecoder.decode(elems)
      actionType <- actionTypeDecoder.decode(elems)
      period <- periodDecoder.decode(elems)
    } yield Action(
      id = rest.actionId,
      competition = rest.competition,
      matchId = rest.matchId,
      kickoffDate = rest.kickoffDate,
      actionType = actionType,
      period = period,
      startTime = rest.startTime,
      endTime = rest.endTime,
      homeOrAway = homeOrAway,
      team = team,
      person = person,
      shirtNum = rest.shirtNum,
      personFunction = rest.personFunction,
      reason = rest.reason,
      extraInfo = rest.extraInfo,
      subPerson = subperson match {
        case Person(0, "NULL") => None
        case sp => Some(sp)
      })
  }

  def importData(dataset: String, config: Config): Source[(Action, Int), NotUsed] = {

    Source.fromIterator(() => Path.of(dataset).toUri.asCsvReader[Action](rfc.withHeader()).iterator.zipWithIndex.filter {
        case (Left(error), index) =>
          log.error("Failed to parse row at line [{}]", index, error)
          false

        case (Right(_), _) =>
          true
      }
      .collect { case (Right(action), index) => (action, index) })

  }

  def aggregatePersonStatistics(action: Action): Unit = {


  }
}

