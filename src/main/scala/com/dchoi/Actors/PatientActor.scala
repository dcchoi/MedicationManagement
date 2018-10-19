package com.dchoi.Actors

import akka.actor.{ Actor, ActorLogging, Props }
import com.dchoi.Models.InitialPatient
import com.dchoi.Service.{ MedicationService, PatientService }

object PatientActor {
  final case class ActionPerformed(description: String)
  final case object GetPatients
  final case class CreatePatient(initialPatient: InitialPatient)
  final case class AssignPatientMedication(patientId: String, medicationId: String)
  final case class UnassignPatientMedication(patientId: String, medicationId: String)

  def props: Props = Props[PatientActor]
}

class PatientActor extends Actor with ActorLogging {
  import PatientActor._

  def receive: Receive = {
    var response = ""

    {
      case GetPatients =>
        sender() ! ActionPerformed(PatientService.outputPatientsInfo())
      case CreatePatient(initialPatient) =>
        if (PatientService.patientExists(initialPatient.id)) {
          response = s"Patient with id ${initialPatient.id} already exists."
        } else {
          PatientService.addPatient(initialPatient)
          response = s"Patient with id ${initialPatient.id} created."
        }
        sender() ! ActionPerformed(response)
      case AssignPatientMedication(patientId, medicationId) =>
        if (!PatientService.patientExists(patientId)) {
          response = s"Patient with id ${patientId} does not exist."
        } else if (!MedicationService.medicationExists(medicationId)) {
          response = s"Medication with id ${medicationId} does not exist."
        } else if (PatientService.patientMedicationExists(patientId, medicationId)) {
          response = s"Medication and Patient pair already exists."
        } else {
          PatientService.assignPatientMedication(patientId, medicationId)
          response = s"Patient with id ${patientId} assigned medication with id ${medicationId}"
        }
        sender() ! ActionPerformed(response)
      case UnassignPatientMedication(patientId, medicationId) =>
        if (!PatientService.patientMedicationExists(patientId, medicationId)) {
          response = s"Medication and Patient pair does not exist"
        } else {
          PatientService.unassignPatientMedication(patientId, medicationId)
          response = s"Patient with id ${patientId} unassigned medication with id ${medicationId}"
        }
        sender() ! ActionPerformed(response)
    }
  }
}
