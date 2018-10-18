package com.dchoi.Models

final case class Patient(id: String, name: String, medications: Seq[Medication])
final case class Patients(patients: Seq[Patient])
