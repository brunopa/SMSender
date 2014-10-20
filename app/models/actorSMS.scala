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

case class SendMessage(url: String, user: String, pass: String, params: JsObject, count: Integer, max: Integer)

class actorSMS extends Actor {

  override def receive: Receive = {

    case SendMessage(url, user, pass, params, count, max) =>
      implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext
/*
      val data = Json.obj(
        "Body" -> "mensage de sms",
        "To" -> "%2B5511983961455",
        "From" -> "%2B12065573610"
      )
*/

      WS.url(url)
        .withAuth(user, pass, WSAuthScheme.BASIC)
        .post(
        Map(
          "Body" -> Seq("mensage de sms"),
          "To" -> Seq("+5511983961455"),
          "From" -> Seq("+12065573610")
        )
      )
        .map(r => Console.println(r.body))

    /*
     WS
            .url(url)
            .withAuth(user, pass, WSAuthScheme.BASIC)
            .post(data)
            .map(r => Console.println(r.body))
            */
  }
}
