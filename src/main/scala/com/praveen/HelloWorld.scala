package com.praveen
import akka.actor._

/**
 * @author ${user.name}
 */
object HelloWorld {

  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {
    println( "Hello Worldq!" )
    println("concat arguments = " + foo(args))
    val system =ActorSystem("TestActorSystem")
    val actor = system.actorOf(Props[worker], name="worker-actor")
    actor ! "hello"
    println("exiting...")
    system.shutdown()
  }

}

class worker extends Actor {
  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    println("Worker Actor in pre-start phase")
  }

  def receive = {
    case "hello" =>
      println("hello world")
    case _ =>
      println("unknown message")
  }

}
