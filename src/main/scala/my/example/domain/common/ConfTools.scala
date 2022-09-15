package my.example.domain.common

import java.io.FileNotFoundException
import java.nio.file.Path
import io.circe.yaml
import io.circe.generic.auto._
import io.circe._
import scala.language.reflectiveCalls
import cats.syntax.either._

import scala.util.{Failure, Success, Try}

object ConfTools {
  def confJson(text: String): Either[ParsingFailure, Json] = yaml.parser.parse(text)

  def parseConf[T](text: String): Either[Error, T] = confJson(text)
    .leftMap(err => err: Error)
    .flatMap(_.as[T])

  def readConfig[T](path: Path)(implicit log: Logging): Either[Throwable, T] = {
    val configText: Try[String] = Tools.read(path).recoverWith {
      case e: FileNotFoundException =>
        log.error(s"Couldn't find configuration file: $path.")
        Failure(e)
    }

    configText.toEither.flatMap(parseConf)

    configText match {
      case Success(v) => parseConf[T](v) match {
        case Right(v) => Right(v)
        case Left(e)  =>
          log.error(s"Couldn't parse configuration file. Error: $e")
          Left(e)
      }
      case Failure(e)    =>
        log.error(s"Failed to read configuration file with error: $e.")
        Left(e)
    }
  }
}
