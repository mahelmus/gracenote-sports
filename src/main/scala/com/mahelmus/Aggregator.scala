package com.mahelmus

import com.mahelmus.domain._
import org.apache.logging.log4j.{LogManager, Logger}
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Attributes
import org.apache.pekko.stream.scaladsl.{Flow, Source}
import org.apache.pekko.{Done, NotUsed}

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZoneId}
import scala.concurrent.Future
import scala.io.Codec
import scala.util.Try

/**
 * Use this trait to add aggregation options to your application
 */
trait Aggregator {
  import Aggregator._

  implicit def system: ActorSystem

  /**
   * Run the stream and log the Player statistics
   * @param dataset CSV file containing [[Action]]s
   * @return
   */
  def run(dataset: String): Future[Done] ={
    val data = importData(dataset)

    data
      .log(name = "streamAggregator")
      .addAttributes(
        Attributes.logLevels(
          onElement = Attributes.LogLevels.Off,
          onFinish = Attributes.LogLevels.Info,
          onFailure = Attributes.LogLevels.Error))
      .groupBy(Int.MaxValue, action => action.person.id)
      .via(playerStats)
      .mergeSubstreams
      .runForeach { aggregatedResult => // Logging stats, which usually will be stored in a DB or sent to Kafka
        log.info("Player [{}]:  [{}]",
          aggregatedResult.map(_._1._2).head,
          aggregatedResult.map(x => (x._1._3, x._2)))
      }
  }
}

object Aggregator {
  private val log: Logger = LogManager.getLogger(this.getClass)

  private val formatter =
    DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm").withZone(ZoneId.of("Europe/Paris"))

  /**
   * Source of input data
   * @param dataset CSV file containing [[Action]]s
   * @return Source of Actions
   */
  def importData(dataset: String): Source[Action, NotUsed] = {
    Source
      .fromIterator(() => scala.io.Source.fromResource(dataset)(Codec.UTF8).getLines())
      .map(row => row.split(",").map(_.trim).toList)
      .drop(1) // drop header
      .collect { case actionId :: competition :: matchId :: kickoffDate :: actionType ::
        period :: startTime :: endTime :: homeOrAway :: teamId :: teamName ::
        personId :: personName :: shirtNum :: personFunction :: reason ::
        extraInfo :: subpersonId :: subpersonName :: Nil =>
        for {
          id <- Try(actionId.toLong).toOption
          mid <- Try(matchId.toLong).toOption
          date <- Try(LocalDate.from(formatter.parse(kickoffDate))).toOption
          team <- Try(Team(teamId.toInt, teamName)).toOption
          person <- Try(Person(personId.toLong, personName)).toOption
        } yield
        Action(
          id = id,
          competition = competition,
          matchId = mid,
          kickoffDate = date,
          actionType = ActionType(actionType),
          period = Period(period),
          startTime = startTime.toLongOption,
          endTime = endTime.toLongOption,
          homeOrAway = HomeOrAway(homeOrAway),
          team = team,
          person = person,
          shirtNum = shirtNum.toByteOption,
          personFunction = personFunction,
          reason = if (reason.trim.toLowerCase == "NULL") None else Some(reason),
          extraInfo = if (extraInfo.trim.toLowerCase == "NULL") None else Some(extraInfo),
          subPerson = Try(Person(subpersonId.toLong, subpersonName)).toOption
        )
      }.collect { case Some(action) => action }
  }

  /**
   * Aggregate player statistics
   * @return Flow of Actions to Player Stats by PLayer and Action type
   */
  def playerStats: Flow[Action, Map[(Long, String, ActionType), Int], NotUsed] =
    Flow[Action].map(action => (action.person.id, action.person.name, action.actionType) -> 1)
      .fold(Map.empty[(Long, String, ActionType), Int]) { (countMap, stats) =>
        val (key, counter) = stats
        countMap + (key -> (countMap.getOrElse(key, 0) + counter))
      }

}

