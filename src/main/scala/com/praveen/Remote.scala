package com.praveen
import akka.remote._
import akka.actor._
import com.typesafe.config.ConfigFactory

case object SubmitJob
case object StartJob

object Server {
  def main(args: Array[String]): Unit = {
    println("starting server")

    val config = ConfigFactory.parseString(
      s"""
      |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
      |akka.remote.enabled-transports = ["akka.remote.netty.tcp"]
      |akka.remote.netty.tcp.hostname = 127.0.0.1
      |akka.remote.netty.tcp.port = 6100
      """.stripMargin)
    val system = ActorSystem("ServerSystem", ConfigFactory.load(config))
    system.actorOf(Props(new ServerA()),"server")
  }
}

class ServerA extends Actor {
  def receive = {
    case SubmitJob =>
      println("submitting job")
  }
}

object Client {
  def main(args: Array[String]): Unit = {
    println("starting client")

    val config = ConfigFactory.parseString(
      s"""
      |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
      |akka.remote.enabled-transports = ["akka.remote.netty.tcp"]
      |akka.remote.netty.tcp.hostname = 127.0.0.1
      |akka.remote.netty.tcp.port = 6101
      """.stripMargin)
    val system = ActorSystem("ClientSystem", ConfigFactory.load(config))
    val client = system.actorOf(Props(new ClientA()),"client")
    client ! StartJob

  }
}

class ClientA extends Actor {
  val remoteServer =
    context.actorSelection("akka.tcp://ServerSystem@127.0.0.1:6100/user/server")
  def receive = {
    case StartJob =>
      println("Starting job")
      remoteServer ! SubmitJob
  }
}

