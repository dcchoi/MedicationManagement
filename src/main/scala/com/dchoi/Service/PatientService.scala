package com.dchoi.Service

import com.dchoi.Models.{ InitialPatient, Medication, Patient }

object PatientService {
  private var patients = Seq.empty[Patient]

  def addPatient(initialPatient: InitialPatient) = {
    if (!patientExists(initialPatient.id)) {
      // Enforce empty medication on patient creation
      patients = patients :+ Patient(initialPatient.id, initialPatient.name, Seq.empty[Medication])
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
        description += "\tMedication Name: " + m.name + "\n"
        description += "\tMedication Id: " + m.id + "\n"
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
