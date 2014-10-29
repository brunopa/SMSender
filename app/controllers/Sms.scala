package controllers
import akka.actor.{Props}
import models.Sms.{ActorSMSTwillo, ActorSMSThrottler, Message}
import models.Campaign
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.libs.Akka
import play.api.Play

object Sms extends Controller {


  implicit val placeWrites: Writes[Campaign] = (
    (JsPath \ "message").write[String] and
      (JsPath \ "phones").write[Seq[Int]]
    )(unlift(Campaign.unapply))

  implicit val campaignReads: Reads[Campaign] = (
    (JsPath \ "message").read[String] and
      (JsPath \ "phones").read[Seq[Int]]
    )(Campaign.apply _)


  def send1(msg: String, phones: String) = Action {
    sendMessages(msg, phones)
    Ok(s"msg = $msg \r phones = $phones")
  }

  def send = Action(BodyParsers.parse.json) { request =>
    val placeResult = request.body.validate[Campaign]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
      },
      campaign => {
        sendMessages(campaign.message, campaign.phones)
        Ok(Json.obj("status" -> "OK", "message" -> (Json.toJson(campaign))))
      }
    )
  }

  def sendMessages(msg: String, phones: String) {
    sendMessages(msg, phones.split(";").map(i => i.toInt))
  }

  def sendMessages(msg: String, phones: Seq[Int]) {

    val ator = Akka.system.actorOf(Props(classOf[ActorSMSThrottler], Akka.system.actorOf(Props[ActorSMSTwillo])))
    val from = Play.current.configuration.getString("twillo.from").get

    phones
      .map(
        i => ator ! Message(from, "+55" + i, msg, 1, 10)
      )
  }
}

/*
curl --include --request POST --header "Content-type: application/json" --data '{"message": "mensagem", "phones":[983961455,971298484]}' http://localhost:9000/send
curl --include --request POST --header "Content-type: application/json" --data '{"message": "mensagem", "phones":[983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,983961455,971298484]}' http://localhost:9000/send


localhost:9000/send2/mensage de sms/983961455;971298484
curl --include --request POST --header "Content-type: application/json" --data '{"message": "mensagem", "phones":["123","456","789"]}' http://radiant-everglades-6796.herokuapp.com/send
http://radiant-everglades-6796.herokuapp.com/send1?msg=String1&fones=String2
http://radiant-everglades-6796.herokuapp.com/send2/String1/String3


name := "akka-throttler"
organization := "akka.pattern.throttle"
version := "1.0-SNAPSHOT"
scalaVersion := "2.9.1"
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
crossScalaVersions := Seq("2.9.2", "2.9.1")
libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor" % "2.0.3" withSources,
  "com.typesafe.akka" % "akka-testkit" % "2.0.3" % "test" withSources,
  "junit" % "junit" % "4.5" % "test" withSources,
  "org.scalatest" %% "scalatest" % "1.6.1" % "test" withSources,
  "com.ning" % "async-http-client" % "1.7.4" withSources
)
*/
