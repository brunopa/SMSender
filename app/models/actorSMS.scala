package models

import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.actor.Status.{Failure, Success}
import com.fasterxml.jackson.databind.util.JSONPObject
import play.api.Play.current
import play.api.libs.json.JsObject
import play.api.libs.ws._
import play.api.libs.ws.ning.NingAsyncHttpClientConfigBuilder
import scala.concurrent.Future
import com.ning.http.client.Realm.AuthScheme
import play.api.libs.json._

case class SendMessage(url: String,
                       user: String,
                       pass: String,
                       from: String,
                       to: String,
                       meesage: String,
                       count: Integer,
                       max: Integer)

class actorSMS extends Actor {

  override def receive: Receive = {

    case SendMessage(url, user, pass, from, to, message, count, max) =>
      implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

      WS
        .url("https://www.google.com.br/search?&q=git")
        .get()
        .map(r => Console.println(r.body.length))

/*
      WS
        .url(url)
        .withAuth(user, pass, WSAuthScheme.BASIC)
        .post(
          Map(
            "Body" -> Seq(message),
            "To" -> Seq(to),
            "From" -> Seq(from)
        )
      )
        .map(r => Console.println(r.body))*/
  }
}
