package com.dchoi

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.dchoi.Actors.{ MedicationActor, PatientActor }

object MyServer extends App with MyRoutes {
  var port = 8090
  if (args.length == 1) {
    port = args(0).toInt
  }

  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val patientActor: ActorRef = system.actorOf(PatientActor.props, "patientActor")
  val medicationActor: ActorRef = system.actorOf(MedicationActor.props, "medicationActor")

  lazy val routes = myRoutes

  Http().bindAndHandle(routes, "localhost", port)

  println(s"Server online at http://localhost:${port}/")

  Await.result(system.whenTerminated, Duration.Inf)

}
