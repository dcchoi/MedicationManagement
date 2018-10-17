package com.dchoi

//#quick-start-server
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.dchoi.Routes.MedicationRoutes

//#main-class
object Server extends App with MedicationRoutes {

  // set up ActorSystem and other dependencies here
  //#main-class
  //#server-bootstrapping
  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  //#server-bootstrapping

  val userRegistryActor: ActorRef = system.actorOf(UserRegistryActor.props, "userRegistryActor")
  val medicationActor: ActorRef = system.actorOf(MedicationActor.props, "medicationActor")

  //#main-class
  lazy val routes: Route = medicationRoutes
  //#main-class

  //#http-server
  Http().bindAndHandle(routes, "localhost", 8002)

  println(s"Server online at http://localhost:8002/")

  Await.result(system.whenTerminated, Duration.Inf)
  //#http-server
  //#main-class
}
//#main-class
//#quick-start-server
