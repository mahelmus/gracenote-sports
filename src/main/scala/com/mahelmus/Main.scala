package com.mahelmus

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.pekko.actor.ActorSystem

import scala.util.{Failure, Success}

object Main extends App with Aggregator {
  private val config: Config = ConfigFactory.load()

  implicit val system: ActorSystem = ActorSystem("gracenote-sports", config)
  import system.dispatcher

  private val log: Logger = LogManager.getLogger(getClass)

  val dataset = config.getString("dataset.filename")

  log.info("Processing started")
  private val process = run(dataset, config)

  process.onComplete {
    case Failure(t) =>
      log.error("Failed to aggregate data", t)
      System.exit(0) // don't run again, wait for manual re-run

    case Success(_) =>
      log.info("Process completed")
      System.exit(0)
  }
}