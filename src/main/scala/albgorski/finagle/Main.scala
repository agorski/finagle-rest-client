package albgorski.finagle

import java.net.URL
import java.nio.charset.StandardCharsets

import com.twitter.finagle.http.RequestBuilder
import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Await, Future}
import org.jboss.netty.handler.codec.http._
import org.slf4j.{Logger, LoggerFactory}

object Main extends App {
  private val logger: Logger = LoggerFactory.getLogger("finagle-client")

  private val client: Service[HttpRequest, HttpResponse] = Http.client.newClient("127.0.0.1:8080,127.0.0.1:8081").toService

  val request1 = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/greeting/1")
  // required by HTTP 1.1 - can have any name
  request1.headers().add(HttpHeaders.Names.HOST, "localhost")

  val request2 = RequestBuilder()
    .url(new URL("http://localhost/greeting/2")) // localhost name is no no important (is for header only)
    .buildGet()

  val response: Future[HttpResponse] = client(request1)
  response onSuccess { resp: HttpResponse =>
    logger.info(s"GET success: $resp")
  } onFailure { fail =>
    logger.error(s"failure: $fail")
  }

  val response2: Future[HttpResponse] = client(request2)

  response2 onSuccess { resp: HttpResponse =>
    logger.info(s"GET success: ${resp.getContent.toString(StandardCharsets.UTF_8)}")
  } onFailure { fail =>
    logger.error(s"failure: $fail")
  }


  Await.all(response, response2)
  client.close()
}
