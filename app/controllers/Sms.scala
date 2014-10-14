package controllers


import akka.actor.Props
import akka.actor.Actor
import akka.actor.Actor._
import akka.actor.ActorSystem
import akka.actor.{ActorSystem, Props}


import models.{SendMessage, actorSMS, Campaign}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.libs.Akka
import play.api.Play

object Sms extends Controller {


  implicit val placeWrites: Writes[Campaign] = (
    (JsPath \ "message").write[String] and
      (JsPath \ "phones").write[Seq[String]]
    )(unlift(Campaign.unapply))

  implicit val campaignReads: Reads[Campaign] = (
    (JsPath \ "message").read[String] and
      (JsPath \ "phones").read[Seq[String]]
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
    sendMessages(msg, phones.split(";"))
  }

  def sendMessages(msg: String, phones: Seq[String]) {

    val ator = Akka.system.actorOf(Props[actorSMS], "myactorSMS1")
    val sid = Play.current.configuration.getString("twillo.sid").get
    val token = Play.current.configuration.getString("twillo.token").get
    val from = Play.current.configuration.getString("twillo.from").get
    val url = Play.current.configuration.getString("twillo.url").get + sid + "/Messages.json"

    phones
      .map(
        i =>
          Json.obj(
            "Body" -> msg,
            "To" -> s"%2B55$i",
            "From" -> from
          )
      )
      .map(
        params => ator ! SendMessage(url, sid, token, params, 1, 10)
      )
  }


  // val futureResponse: Future[Response] = WS.url(url).post(data)


}

/*
localhost:9000/send2/mensage de sms/983961455;971298484
curl --include --request POST --header "Content-type: application/json" --data '{"message": "mensagem", "phones":["123","456","789"]}' http://radiant-everglades-6796.herokuapp.com/send
http://radiant-everglades-6796.herokuapp.com/send1?msg=String1&fones=String2
http://radiant-everglades-6796.herokuapp.com/send2/String1/String3
+55 41 3908-8447
* */
