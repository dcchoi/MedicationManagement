package com.dchoi

//#quick-start-server
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

//#main-class
object MyServer extends App with MyRoutes {

  // set up ActorSystem and other dependencies here
  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val patientActor: ActorRef = system.actorOf(PatientActor.props, "patientActor")
  val medicationActor: ActorRef = system.actorOf(MedicationActor.props, "medicationActor")

  lazy val routes = myRoutes

  Http().bindAndHandle(routes, "localhost", 8002)

  println(s"Server online at http://localhost:8002/")

  Await.result(system.whenTerminated, Duration.Inf)

}
