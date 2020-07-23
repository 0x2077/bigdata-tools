package my.example.domain

object Main {
  def main(args: Array[String]): Unit = {
    args.head match {
      case "--appstat" => (new YarnClient).getAppStat(args.tail)
    }
  }
}
