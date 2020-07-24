package my.example.domain

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class YarnClientTest extends FlatSpec with Matchers {

  trait MockOutput extends Output {
    var messages: Seq[String] = Seq()

    override def print(s: String) = {
      messages = messages :+ s
    }
  }

  "yarnClient" should "get app stat from Yarn" in {
    val args: Array[String] = Array(
      "--app-id", "application_1581421691399_0001",
      "--base-url", "http://localhost:8088"
    )
    val yc = new YarnClient with MockOutput
    val res = yc.getAppStat(args)
    Await.ready(res, 30.seconds)
    yc.messages shouldBe List("Found Spark application: application_1581421691399_0001", "VCoreSeconds: 371", "MBSeconds: 631846", "ElapsedTimeMs: 129831")
  }

  "yarnClient" should "get app stat from Yarn and format output as csv" in {
    val args: Array[String] = Array(
      "--app-id", "application_1581421691399_0001",
      "--base-url", "http://localhost:8088",
      "--output-format", "csv"
    )
    val yc = new YarnClient with MockOutput
    val res = yc.getAppStat(args)
    Await.ready(res, 30.seconds)
    yc.messages shouldBe List("371,631846,129831")
  }
}
