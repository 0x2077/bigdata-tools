package my.example.domain

import org.scalatest.{FlatSpec, Matchers}

class MainTest extends FlatSpec with Matchers {

  "Main called with --appstat" should "get app stat from Yarn" in {
    val args: Array[String] = Array(
      "--appstat",
      "--app-id", "application_1581421691399_0001",
      "--base-url", "http://localhost:8088"
    )
    Main.main(args)
  }
}
