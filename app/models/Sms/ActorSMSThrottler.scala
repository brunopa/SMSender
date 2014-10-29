package models.Sms

import java.util.concurrent.TimeUnit

import akka.actor.Actor.Receive
import akka.actor.{ActorRef, Props, Actor}
import models.akka.pattern.throttle.Throttler.{Queue, SetTarget, Rate}
import models.akka.pattern.throttle.TimerBasedThrottler
import play.libs.Akka

import scala.concurrent.duration.Duration

class ActorSMSThrottler(target: ActorRef)extends Actor {

  val throttler = Akka.system.actorOf(Props(
    new TimerBasedThrottler(new Rate(5, Duration(1, TimeUnit.SECONDS))
    )))

  throttler ! SetTarget(Some(target))

  override def receive: Receive = {

    case m @ Message(from, to, message, count, max) =>
      throttler ! Queue(m)
  }

}
