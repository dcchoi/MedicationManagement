package com.dchoi

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{ get, post }
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout
import com.dchoi.MedicationActor._
import com.dchoi.Models._
import com.dchoi.PatientActor.{ AssignPatientMedication, CreatePatient, GetPatients }
import com.dchoi.Service.PatientService

import scala.concurrent.Future
import scala.concurrent.duration._

trait MyRoutes extends JsonSupport {

  // we leave these abstract, since they will be provided by the App
  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[MyRoutes])

  // other dependencies that MyRoutes use
  def medicationActor: ActorRef
  def patientActor: ActorRef

  implicit lazy val timeout = Timeout(20.seconds) // usually we'd obtain the timeout from the system's configuration

  lazy val myRoutes: Route =
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
                val medicationCreated: Future[MedicationActor.ActionPerformed] =
                  (medicationActor ? CreateMedication(medication)).mapTo[MedicationActor.ActionPerformed]
                onSuccess(medicationCreated) { performed =>
                  log.info("Created medication [{}]: {}", medication.name, performed.description)
                  complete((StatusCodes.Created, performed))
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
                val patients: Future[Patients] =
                  (patientActor ? GetPatients).mapTo[Patients]
                complete(patients)
              },
              post {
                entity(as[Patient]) { patient =>
                  val patientCreated: Future[PatientActor.ActionPerformed] =
                    (patientActor ? CreatePatient(patient)).mapTo[PatientActor.ActionPerformed]
                  onSuccess(patientCreated) { performed =>
                    log.info("Created patient [{}]: {}", patient.name, performed.description)
                    complete((StatusCodes.Created, performed))
                  }
                }
              },
              put {
                entity(as[PatientMedicationUpdate]) { update =>
                  val patientMedicationUpdate: Future[PatientMedicationUpdate] =
                    (patientActor ? AssignPatientMedication(update.patientId, update.medicationId)).mapTo[PatientMedicationUpdate]
                  complete(patientMedicationUpdate)
                }
              }
            )
          }
        )
      }
}
