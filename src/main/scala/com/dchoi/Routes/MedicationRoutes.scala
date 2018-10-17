package com.dchoi.Routes

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging

import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.delete
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.directives.PathDirectives.path

import scala.concurrent.Future
import com.dchoi.MedicationActor._
import akka.pattern.ask
import akka.util.Timeout
import com.dchoi.JsonSupport
import com.dchoi.Models.{ Medication, Medications }

trait MedicationRoutes extends JsonSupport {

  // we leave these abstract, since they will be provided by the App
  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[MedicationRoutes])

  // other dependencies that MedicationRoutes use
  def medicationActor: ActorRef

  // Required by the `ask` (?) method below
  implicit lazy val timeout = Timeout(20.seconds) // usually we'd obtain the timeout from the system's configuration

  lazy val medicationRoutes: Route =
    pathPrefix("medications") {
      concat(
        pathEnd {
          concat(
            get {
              val medications: Future[Medications] =
                (medicationActor ? GetMedications).mapTo[Medications]
              complete(medications)
            },
            post {
              entity(as[Medication]) { medication =>
                val medicationCreated: Future[ActionPerformed] =
                  (medicationActor ? CreateMedication(medication)).mapTo[ActionPerformed]
                onSuccess(medicationCreated) { performed =>
                  log.info("Created medication [{}]: {}", medication.name, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            }
          )
        }
      )
    }
}
