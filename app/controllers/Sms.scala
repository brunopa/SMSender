package controllers


import models.Campaign
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

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

curl --include --request POST --header "Content-type: application/json" --data '{"message": "mensagem", "phones":["123","456","789"]}' http://localhost:9000/send
http://localhost:9000/send1?msg=String1&fones=String2
http://localhost:9000/send2/String1/String3
* */
