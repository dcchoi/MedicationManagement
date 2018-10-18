package com.dchoi.Service

import com.dchoi.Models.{ Medication, Patient }
import scala.collection.mutable.Seq

object PatientService {
  private var patients = Seq.empty[Patient]

  def retrievePatients(): Seq[Patient] = {
    patients
  }

  def addPatient(patient: Patient) = {
    patients = patients :+ patient
  }

  def assignPatientMedication(patientId: String, medicationId: String) = {
    patients = patients.map(targetPatient => {
      if (targetPatient.id == patientId) {
        Patient(targetPatient.id, targetPatient.name, targetPatient.medications :+ Medication("test", "test"))
      } else {
        targetPatient
      }
    })
  }

  def outputPatientsInfo(): String = {
    var description = ""
    patients.foreach(p => {
      description += "Patient Name: " + p.name + "\n"
      p.medications.foreach(m => {
        description += "\tMedication: " + m.name + "\n"
        description += "\tId: " + m.id + "\n"
      })
    })
    return description
  }

}
