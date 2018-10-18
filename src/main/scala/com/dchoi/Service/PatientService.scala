package com.dchoi.Service

import com.dchoi.Models.{ Medication, Patient }

object PatientService {
  private var patients = Seq.empty[Patient]

  def retrievePatients(): Seq[Patient] = {
    patients
  }

  def addPatient(patient: Patient) = {
    patients = patients :+ patient
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
    return description
  }

}
