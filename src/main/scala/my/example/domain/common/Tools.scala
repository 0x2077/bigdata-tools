package my.example.domain.common

import java.nio.file.Path
import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success, Try}

object Tools {
  def use[T <: { def close(): Unit }, V](resource: T)(code: T => V): V = {
    try {
      code(resource)
    } finally {
      resource.close()
    }
  }

  def read(path: Path): Try[String] = {
    Try {
      val fileReader: BufferedSource => String = (s: BufferedSource) => s.mkString("")
      val file                                 = Source.fromFile(path.toUri)

      use(file)(fileReader)
    }
  }

  def withLoggedTry[T](fn: => T, msgPrefix: String)(implicit log: Logging): Try[T] = {
    Try(fn).transform(
      s => {
        log.info(s"$msgPrefix succeed: $s")
        Success(s)
      },
      exception => {
        log.error(s"$msgPrefix failed with exception: $exception")
        Failure(exception)
      }
    )
  }

  @scala.annotation.tailrec
  def retry[T](n: Int)(fn: => T)(implicit log: Logging): T = {
    util.Try { fn } match {
      case util.Success(x) => x
      case util.Failure(e: Exception) if n > 1 =>
        log.warn(s"Execution failed: ${e.getCause}. Retries left $n.")
        Thread.sleep(Const.RetryWaitMs)
        retry(n - 1)(fn)
      case util.Failure(e: Exception) => throw e
    }
  }
}
