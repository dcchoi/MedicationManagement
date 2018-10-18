package com.dchoi.Service

import com.dchoi.Models.Medication

import scala.collection.mutable.Seq

object MedicationService {
  private val medications = Seq.empty[Medication]

  def retrieveMedications(): Seq[Medication] = {
    medications
  }

  def addMedication(medication: Medication) = {
    medications :+ medication
  }

  def outputMedicationsInfo(): String = {
    var description = ""
    medications.foreach(m => {
      description += "Medication: " + m.name + "\n"
      description += "Id: " + m.id + "\n"
    })
    return description
  }
}
