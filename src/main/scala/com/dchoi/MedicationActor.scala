package com.dchoi

import com.dchoi.Models.{ Medication, Medications, Patients }
import akka.actor.{ Actor, ActorLogging, Props }
import com.dchoi.Service.MedicationService

object MedicationActor {

  final case class ActionPerformed(description: String)

  final case object GetMedications

  final case class CreateMedication(medication: Medication)

  def props: Props = Props[MedicationActor]
}

class MedicationActor extends Actor with ActorLogging {

  import MedicationActor._

  def receive: Receive = {
    var response = ""

    {
      case GetMedications =>
        sender() ! ActionPerformed(MedicationService.outputMedicationsInfo())
      case CreateMedication(medication) =>
        if (MedicationService.medicationExists(medication.id)) {
          response = s"Medication with id ${medication.id} already exists."
        } else {
          MedicationService.addMedication(medication)
          response = s"Medication ${medication.name} created."
        }
        sender() ! ActionPerformed(response)
    }
  }

}
