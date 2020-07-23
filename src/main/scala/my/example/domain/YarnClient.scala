package my.example.domain

// In addition to the usual values brought into scope by `import sttp.client._`,
// the `quick` version also defines a default implicit backend.
import sttp.client.{Request, Response, ResponseError, SttpBackend}
import sttp.client.quick._
// Circe integration: `asJson` response description.
import sttp.client.circe._

import io.circe.generic.auto._

import sttp.client.asynchttpclient.WebSocketHandler
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import scala.concurrent.ExecutionContext.Implicits.global

// Case classes corresponding to the json returned by Yarn (just the
// fields that interest us).
case class YarnAppInfoResponse(
                                id: String,
                                user: String,
                                name: String,
                                queue: String,
                                state: String,
                                finalStatus: String,
                                progress: Double,
                                trackingUI: String,
                                trackingUrl: String,
                                diagnostics: String,
                                clusterId: Long,
                                applicationType: String,
                                applicationTags: String,
                                startedTime: Long,
                                finishedTime: Long,
                                elapsedTime: Long,
                                amContainerLogs: String,
                                amHostHttpAddress: String,
                                allocatedMB: Int,
                                allocatedVCores: Int,
                                reservedMB: Int,
                                reservedVCores: Int,
                                runningContainers: Int,
                                memorySeconds: Long,
                                vcoreSeconds: Long,
                                preemptedResourceMB: Int,
                                preemptedResourceVCores: Int,
                                numNonAMContainerPreempted: Int,
                                numAMContainerPreempted: Int,
                                logAggregationStatus: String
                              )
case class YarnAppResponse(app: YarnAppInfoResponse)


object YarnClient {
  def getAppStat(args: Array[String]) = {
    val conf = new YarnClientConf(args)

    val baseUrl = conf.baseUrl.toOption.get
    val appId = conf.appId.toOption.get

    implicit val backend: SttpBackend[Future, Nothing, WebSocketHandler] = AsyncHttpClientFutureBackend()

    // Describing the request: specifying the method, uri.
    // The `query` parameter is automatically url-encoded
    // `sort` will be unwrapped if `Some(_)`, and removed if `None`.
    val request: Request[Either[String, String], Nothing] =
      basicRequest
        .get(uri"$baseUrl/ws/v1/cluster/apps/$appId")

    // Describing how to handle the response.
    val resp: Future[Response[Either[ResponseError[io.circe.Error], YarnAppResponse]]] =
      request.response(asJson[YarnAppResponse]).send()

    Await.ready(resp, 30.seconds)

    resp.map { response =>
      // The body will be a `Left(_)` in case of a non-2xx response, or a json
      // deserialization error. It will be `Right(_)` otherwise.
      response.body match {
        case Left(error) => println(s"Error when executing request: $error")
        case Right(data) =>
          println(s"Found Spark application: ${data.app.id}")
          println(s"VCoreSeconds: ${data.app.vcoreSeconds}")
          println(s"MBSeconds: ${data.app.memorySeconds}")
          println(s"ElapsedTimeMs: ${data.app.elapsedTime}")
      }
    }
  }
}
