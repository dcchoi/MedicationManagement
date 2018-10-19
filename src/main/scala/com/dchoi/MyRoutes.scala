package com.dchoi

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{ get, post }
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout
import com.dchoi.MedicationActor._
import com.dchoi.Models._
import com.dchoi.PatientActor.{ AssignPatientMedication, CreatePatient, GetPatients, UnassignPatientMedication }

import scala.concurrent.Future
import scala.concurrent.duration._

trait MyRoutes extends JsonSupport {

  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[MyRoutes])

  def medicationActor: ActorRef

  def patientActor: ActorRef

  implicit lazy val timeout = Timeout(5.seconds)

  lazy val myRoutes: Route =
    pathPrefix("medications") {
      concat(
        pathEnd {
          concat(
            get {
              val medications: Future[MedicationActor.ActionPerformed] =
                (medicationActor ? GetMedications).mapTo[MedicationActor.ActionPerformed]
              onSuccess(medications) { performed =>
                complete(StatusCodes.OK, performed.description)
              }
            },
            post {
              entity(as[Medication]) { medication =>
                val medicationCreated: Future[MedicationActor.ActionPerformed] =
                  (medicationActor ? CreateMedication(medication)).mapTo[MedicationActor.ActionPerformed]
                onSuccess(medicationCreated) { performed =>
                  complete((StatusCodes.Created, performed.description))
                }
              }
            }
          )
        }
      )
    } ~
      pathPrefix("patients") {
        concat(
          pathEnd {
            concat(
              get {
                val patientsRetrieved: Future[PatientActor.ActionPerformed] =
                  (patientActor ? GetPatients).mapTo[PatientActor.ActionPerformed]
                onSuccess(patientsRetrieved) { performed =>
                  complete(StatusCodes.OK, performed.description)
                }
              },
              post {
                entity(as[InitialPatient]) { initialPatient =>
                  val patientCreated: Future[PatientActor.ActionPerformed] =
                    (patientActor ? CreatePatient(initialPatient)).mapTo[PatientActor.ActionPerformed]
                  onSuccess(patientCreated) { performed =>
                    complete((StatusCodes.Created, performed.description))
                  }
                }
              }
            )
          } ~
            path("assign") {
              post {
                entity(as[PatientMedicationUpdate]) { update =>
                  val patientMedicationUpdate: Future[PatientActor.ActionPerformed] =
                    (patientActor ? AssignPatientMedication(update.patientId, update.medicationId)).mapTo[PatientActor.ActionPerformed]
                  onSuccess(patientMedicationUpdate) { performed =>
                    complete((StatusCodes.OK, performed.description))
                  }
                }
              }
            } ~
            path("unassign") {
              post {
                entity(as[PatientMedicationUpdate]) { update =>
                  val patientMedicationUpdate: Future[PatientActor.ActionPerformed] =
                    (patientActor ? UnassignPatientMedication(update.patientId, update.medicationId)).mapTo[PatientActor.ActionPerformed]
                  onSuccess(patientMedicationUpdate) { performed =>
                    complete((StatusCodes.OK, performed.description))
                  }
                }
              }
            }
        )
      }
}
