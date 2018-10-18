package com.dchoi

import com.dchoi.Models.{ Medication, Medications, Patient, Patients }
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{ DefaultJsonProtocol, JsonFormat }

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  //implicit val patientJsonFormat = jsonFormat3(Patient)
  implicit val medicationFormat = jsonFormat(Medication, "id", "name")
  implicit val patientFormat = jsonFormat(Patient, "id", "name", "medications")
  implicit val patientsJsonFormat = jsonFormat(Patients, "patients")
  implicit val medicationsJsonFormat = jsonFormat(Medications, "medications")

  implicit val patientActionPerformedJsonFormat = jsonFormat1(PatientActor.ActionPerformed)
  implicit val medicationActionPerformedJsonFormat = jsonFormat1(MedicationActor.ActionPerformed)

}
