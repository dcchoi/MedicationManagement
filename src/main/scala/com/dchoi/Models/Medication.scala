package com.dchoi.Models

final case class Medication(id: String, name: String)
final case class Medications(medications: Seq[Medication])
