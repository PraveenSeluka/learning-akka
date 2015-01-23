package com.praveen

import akka.actor.{ActorRef, Props, Actor, ActorSystem}

object PingPong {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("TestActorSystem")
    val pongActor = system.actorOf(Props(new PongActor),"PingActor")
    val pingActor = system.actorOf(Props(new PingActor(pongActor)), "PongActor")
    pingActor ! PingMessage
    //let them play for some time
    Thread.sleep(2000)
    pingActor ! StopPlaying
    system.shutdown()
  }
}
case object PingMessage
case object PongMessage
case object StopPlaying
class PingActor(pongActor: ActorRef) extends Actor {
  def receive = {
    case PingMessage =>
      println("ping")
      pongActor ! PongMessage
    case StopPlaying =>
      context.stop(self)
  }
}
class PongActor extends Actor {
  def receive = {
    case PongMessage =>
      println("pong")
      sender ! PingMessage
  }
}

