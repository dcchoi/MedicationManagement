package com.dchoi

import com.dchoi.Models.{ Patient, PatientMedicationUpdate, Patients }
import akka.actor.{ Actor, ActorLogging, Props }
import com.dchoi.Service.PatientService

object PatientActor {
  final case class ActionPerformed(description: String)
  final case object GetPatients
  final case class CreatePatient(patient: Patient)
  final case class AssignPatientMedication(patientId: String, medicationId: String)

  def props: Props = Props[PatientActor]
}

class PatientActor extends Actor with ActorLogging {
  import PatientActor._

  def receive: Receive = {
    case GetPatients =>
      print(PatientService.outputPatientsInfo())
      sender() ! Patients(PatientService.retrievePatients())
    case CreatePatient(patient) =>
      PatientService.addPatient(patient)
      sender() ! ActionPerformed(s"Patient ${patient.name} created.")
    case AssignPatientMedication(patientId, medicationId) =>
      PatientService.assignPatientMedication(patientId, medicationId)
      sender() ! PatientMedicationUpdate(patientId, medicationId)
  }

}
