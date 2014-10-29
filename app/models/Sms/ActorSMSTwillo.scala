package models.Sms

import _root_.akka.actor.Actor
import play.api.Play
import play.api.Play.current
import play.api.libs.ws._

class ActorSMSTwillo extends Actor {

  val sid = Play.current.configuration.getString("twillo.sid").get+ "ewqwerwer"
  val token = Play.current.configuration.getString("twillo.token").get + "qweqwe"
  val url = Play.current.configuration.getString("twillo.url").get + sid + "/Messages.json"

  override def receive: Receive = {

    case Message(from, to, message, count, max) =>
      implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext


      Console.println(Math.random().toString)
/*
      WS
        .url(url)
        .withAuth(sid, token, WSAuthScheme.BASIC)
        .post(
          Map(
            "Body" -> Seq(message),
            "To" -> Seq(to),
            "From" -> Seq(from)
          )
        )
        .map(r => Console.println(r.body + Math.random().toString))*/
  }
}
