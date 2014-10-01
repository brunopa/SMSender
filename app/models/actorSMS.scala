package models

import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.actor.Status.{Failure, Success}
import play.api.Play.current
import play.api.libs.ws._
import play.api.libs.ws.ning.NingAsyncHttpClientConfigBuilder
import scala.concurrent.Future


case class SendMessage(from: String, to: String, count: Int)

class actorSMS extends Actor {

  override def receive: Receive = {

    case SendMessage(from, to, count) =>
      implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

      WS.url("https://www.google.com.br/?q=teste")
        .get()
        .map(response => Console.println(response.body.length))
  }
}
