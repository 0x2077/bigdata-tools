package my.example.domain

import org.rogach.scallop.{ScallopConf, ScallopOption}

class YarnClientConf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val baseUrl: ScallopOption[String] = opt[String](required = true)
  val appId: ScallopOption[String] = opt[String](required = true)
  val outputFormat: ScallopOption[String] = opt[String](default=Some("text"))
  verify()
}
