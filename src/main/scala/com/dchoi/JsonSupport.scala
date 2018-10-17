package com.dchoi

import com.dchoi.Models.{ Medication, Medications }
import com.dchoi.UserRegistryActor.ActionPerformed
import com.dchoi.MedicationActor.ActionPerformed

//#json-support
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val medicationJsonFormat = jsonFormat2(Medication)
  implicit val medicationsJsonFormat = jsonFormat1(Medications)

  implicit val actionPerformedJsonFormat = jsonFormat1(UserRegistryActor.ActionPerformed)
  implicit val medicationActionPerformedJsonFormat = jsonFormat1(MedicationActor.ActionPerformed)

}
//#json-support
