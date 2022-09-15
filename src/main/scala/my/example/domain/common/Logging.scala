package my.example.domain.common

trait Logging {
  def info(msg: String)
  def error(msg: String)
  def warn(msg: String)
}
