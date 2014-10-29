package models.Sms

case class Message(from: String,
                   to: String,
                   message: String,
                   count: Integer,
                   max: Integer)