package com.dchoi

import com.dchoi.Models.{ Patient, Patients }
import akka.actor.{ Actor, ActorLogging, Props }

object PatientActor {
  final case class ActionPerformed(description: String)
  final case object GetPatients
  final case class CreatePatient(patient: Patient)

  def props: Props = Props[PatientActor]
}

class PatientActor extends Actor with ActorLogging {
  import PatientActor._

  var patients = Set.empty[Patient]

  def receive: Receive = {
    case GetPatients =>
      print(outputPatients(Patients(patients.toSeq)))
      sender() ! Patients(patients.toSeq)
    case CreatePatient(patient) =>
      patients += patient
      sender() ! ActionPerformed(s"Patient ${patient.name} created.")
  }

  def outputPatients(patientList: Patients): String = {
    var description = ""
    patientList.patients.foreach(p => {
      description += "Patient Name: " + p.name + "\n"
      p.medications.foreach(m => {
        description += "\tMedication: " + m.name + "\n"
        description += "\tId: " + m.id + "\n"
      })
    })
    return description
  }

}
