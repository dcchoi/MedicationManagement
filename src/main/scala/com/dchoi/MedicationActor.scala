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
    case GetMedications =>
      sender() ! Medications(MedicationService.retrieveMedications())
    case CreateMedication(medication) =>
      MedicationService.addMedication(medication)
      sender() ! ActionPerformed(s"Medication ${medication.name} created.")
  }

}
