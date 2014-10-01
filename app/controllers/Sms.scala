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
    val ator = Akka.system.actorOf(Props[actorSMS], "myactorSMS")
    ator ! SendMessage("", "", 0)
    ator ! SendMessage("", "", 0)
    ator ! SendMessage("", "", 0)
    ator ! SendMessage("", "", 0)
    ator ! SendMessage("", "", 0)
    ator ! SendMessage("", "", 0)
    ator ! SendMessage("", "", 0)
    ator ! SendMessage("", "", 0)
    ator ! SendMessage("", "", 0)

    Console.println("fim actors")
    Ok(s"msg = $msg \r phones = $phones")
  }

  def send = Action(BodyParsers.parse.json) { request =>
    val placeResult = request.body.validate[Campaign]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toFlatJson(errors)))
      },
      campaign => {
        //Place.save(place)
        Ok(Json.obj("status" ->"OK", "message" -> (Json.toJson(campaign) ) ))
      }
    )
  }
}

/*
curl --include --request POST --header "Content-type: application/json" --data '{"message": "mensagem", "phones":["123","456","789"]}' http://radiant-everglades-6796.herokuapp.com/send
http://radiant-everglades-6796.herokuapp.com/send1?msg=String1&fones=String2
http://radiant-everglades-6796.herokuapp.com/send2/String1/String3
+55 41 3908-8447
* */
