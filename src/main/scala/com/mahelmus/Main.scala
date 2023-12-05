package com.mahelmus

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.pekko.actor.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

/**
 * Streaming application that reads data from a CSV file and calculates
 * player statistics.
 */
object Main extends App with Aggregator {
  private val config: Config = ConfigFactory.load()

  implicit val system: ActorSystem = ActorSystem("gracenote-sports", config)
  import system.dispatcher

  private val log: Logger = LogManager.getLogger(this.getClass)

  val dataset = config.getString("dataset.filename")

  log.info("Processing started")
  private val process = run(dataset)

  Await.ready(process, Duration.Inf)

  process.onComplete {
    case Failure(t) =>
      log.error("Failed to aggregate data", t)
      system.terminate()

    case Success(_) =>
      log.info("Process completed")
      system.terminate()
  }
}