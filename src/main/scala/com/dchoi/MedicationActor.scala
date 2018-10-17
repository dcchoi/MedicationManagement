package com.dchoi

import com.dchoi.Models.{ Medication, Medications }
import akka.actor.{ Actor, ActorLogging, Props }

object MedicationActor {
  final case class ActionPerformed(description: String)
  final case object GetMedications
  final case class CreateMedication(medication: Medication)
  final case class GetMedication(name: String)
  final case class DeleteMedication(name: String)

  def props: Props = Props[MedicationActor]
}

class MedicationActor extends Actor with ActorLogging {
  import MedicationActor._

  var medications = Set.empty[Medication]

  def receive: Receive = {
    case GetMedications =>
      sender() ! Medications(medications.toSeq)
    case CreateMedication(medication) =>
      medications += medication
      sender() ! ActionPerformed(s"Medication ${medication.name} created.")
    //    case GetMedication(id) =>
    //      sender() ! medications.find(_.id == id)
    //    case DeleteMedication(id) =>
    //      medications.find(_.id == id) foreach { medication => medications -= medication }
    //      sender() ! ActionPerformed(s"Medication ${id} deleted.")
  }

}