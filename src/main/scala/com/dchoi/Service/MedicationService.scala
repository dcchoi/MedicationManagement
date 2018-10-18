package com.dchoi.Service

import com.dchoi.Models.Medication

object MedicationService {
  private var medications = Seq.empty[Medication]

  def retrieveMedications(): Seq[Medication] = {
    medications
  }

  def addMedication(medication: Medication) = {
    medications = medications :+ medication
  }

  def retrieveMedication(medicationId: String): Medication = {
    medications.filter(m => m.id == medicationId).head
  }

  def outputMedicationsInfo(): String = {
    var description = ""
    medications.foreach(m => {
      description += "Medication: " + m.name + "\n"
      description += "Medication Id: " + m.id + "\n"
    })
    return description
  }
}
