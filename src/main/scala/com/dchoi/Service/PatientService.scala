package com.dchoi.Service

import com.dchoi.Models.{ Patient }

object PatientService {
  private var patients = Seq.empty[Patient]

  def addPatient(patient: Patient) = {
    if (patients.filter(p => p.id == patient.id).length == 0) {
      patients = patients :+ patient
    } else {
      println(s"Patient with id: ${patient.id} already exists")
    }
  }

  def assignPatientMedication(patientId: String, medicationId: String) = {
    val medication = MedicationService.retrieveMedication(medicationId)

    patients = patients.map(targetPatient => {
      if (targetPatient.id == patientId && medication != null) {
        Patient(targetPatient.id, targetPatient.name, targetPatient.medications :+ medication)
      } else {
        targetPatient
      }
    })
  }

  def unassignPatientMedication(patientId: String, medicationId: String) = {
    val medication = MedicationService.retrieveMedication(medicationId)

    patients = patients.map(targetPatient => {
      if (targetPatient.id == patientId && medication != null) {
        Patient(targetPatient.id, targetPatient.name, targetPatient.medications.filterNot(m => m.id == medicationId))
      } else {
        targetPatient
      }
    })
  }

  def outputPatientsInfo(): String = {
    var description = ""
    patients.foreach(p => {
      description += "Patient Name: " + p.name + "\n"
      description += "Patient Id: " + p.id + "\n"
      p.medications.foreach(m => {
        description += "\tMedication: " + m.name + "\n"
        description += "\tId: " + m.id + "\n"
      })
    })
    description
  }

  def getPatient(patientId: String): Patient = {
    patients.filter(p => p.id == patientId).head
  }

  def patientExists(patientId: String): Boolean = {
    patients.filter(p => p.id == patientId).length > 0
  }

  def patientMedicationExists(patientId: String, medicationId: String): Boolean = {
    if (!patientExists(patientId)) {
      return false
    }
    getPatient(patientId).medications.filter(m => m.id == medicationId).length > 0
  }
}
