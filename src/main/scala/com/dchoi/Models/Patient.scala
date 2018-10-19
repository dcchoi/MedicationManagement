package com.dchoi.Models

final case class InitialPatient(id: String, name: String)
final case class Patient(id: String, name: String, medications: Seq[Medication])
final case class Patients(patients: Seq[Patient])
final case class PatientMedicationUpdate(patientId: String, medicationId: String)
