package com.mahelmus

import com.mahelmus.domain.ActionType._
import com.mahelmus.domain.{Action, ActionType, Person}
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.scaladsl.Sink
import org.apache.pekko.testkit.TestKit
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class AggregatorSpec extends TestKit(ActorSystem())
  with AnyWordSpecLike
  with Matchers
  with ScalaFutures {

  val dataset = "testDataset.csv"

  "Aggregator" should {
    "import data correctly" in {
      val result = Aggregator.importData(dataset).runWith(Sink.seq)

      whenReady(result) {
        case seq: Seq[Action] =>
          seq.size shouldBe 20
          seq.head.person shouldBe Person(1223164, "Sheraldo Becker")
      }
    }

    "calculate Player statistics correctly" in {
      val result = Aggregator.importData(dataset).via(Aggregator.playerStats).runWith(Sink.seq)

      whenReady(result) {
        case seq: Seq[Map[(Long, String, ActionType), Int]] =>
          seq.size shouldBe 1
          seq.head shouldBe Map(
            (901784, "Ludcinio Marengo", LineUp) -> 1,
            (682493, "Donny Gorter", Corner) -> 1,
            (682493, "Donny Gorter", FreeKick) -> 1,
            (1386239, "Lukas Gˆrtler", LineUp) -> 1,
            (1527072, "Nick Venema", LineUp) -> 1,
            (1508170, "Trevor David", LineUp) -> 1,
            (1385408, "Giovanni TroupÈe", LineUp) -> 1,
            (1220621, "Melvyn Lorenzen", Substitution) -> 1,
            (796836, "Dario Dumic", LineUp) -> 1,
            (1515562, "Odysseus Velanas", LineUp) -> 1,
            (769254, "Christiaan Bax", LineUp) -> 1,
            (917392, "Rens Bluemink", LineUp) -> 1,
            (1223164, "Sheraldo Becker", FoulCommitted) -> 1,
            (579307, "Urby Emanuelson", Substitution) -> 1,
            (924566, "Anouar Kali", FoulCommitted) -> 1,
            (474240, "Edson Braafheid", FoulCommitted) -> 1,
            (924566, "Anouar Kali", FreeKick) -> 1,
            (664080, "Lex Immers", FoulCommitted) -> 2,
            (1476074, "Dennis van der Heijden", LineUp) -> 1)
      }
    }
  }

}
