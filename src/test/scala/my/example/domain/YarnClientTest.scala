package my.example.domain

import org.scalatest.{FlatSpec, Matchers}

class YarnClientTest extends FlatSpec with Matchers {

  "yarnClient" should "get app stat from Yarn" in {
    val args: Array[String] = Array(
      "--app-id", "application_1581421691399_0001",
      "--base-url", "http://localhost:8088"
    )
    YarnClient.getAppStat(args)
  }
}
