package com.dchoi.Models
import collection.mutable.Seq

final case class Medication(id: String, name: String)
final case class Medications(medications: Seq[Medication])
