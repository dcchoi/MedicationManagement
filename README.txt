Overview:

This Medication Management REST API works with the following technologies:
Akka HTTP framework (https://doc.akka.io/docs/akka-http/current/introduction.html)
Scala
SBT - Used to import third party libraries

--------------------------------

Prerequisites:

SBT (Simple Build Tool) - https://www.scala-sbt.org/
Scala - https://www.scala-lang.org/

For Debugging and overall project management I use IntelliJ. you can download the free community version here:
https://www.jetbrains.com/idea/download/#section=windows

--------------------------------

Running the server:

I have attached a prebuilt jar that can be used right away called: MedicationManagement-assembly-0.1-SNAPSHOT.jar

For my requests to the REST API, work with Window's Powershell to hit requests. For Unix machines, you will need to have the Curl equivalent.

From command line, on the same directory as MedicationManagement-assembly-0.1-SNAPSHOT.jar, type

"scala MedicationManagement-assembly-0.1-SNAPSHOT <port number>" (without quotes) to start the server up

If you want to specify a particular port fill in the <port number>, else it will default to 8090

--------------------------------

Following use cases that the API can perform:

- Add a new medication to the list of available medications

Invoke-RestMethod -Uri http://localhost:8090/medications -Body '{"id": "1", "name": "Vita"}' -Method POST -ContentType application/json

- Create a new patient

Invoke-RestMethod -Uri http://localhost:8090/patients -Body '{"id": "a", "name": "Bill"}' -Method POST -ContentType application/json

- Assign a patient a new medication from the list of available medications

Invoke-RestMethod -Uri http://localhost:8090/patients/assign -Body '{"patientId": "a", "medicationId": "1"}' -Method POST -ContentType application/json

- Remove a medication from the list of medications assigned to a patient

Invoke-RestMethod -Uri http://localhost:8090/patients/unassign -Body '{"patientId": "a", "medicationId": "1"}' -Method POST -ContentType application/json

- View Patients along with the Medications those Patients have

Invoke-RestMethod -Uri http://localhost:8090/patients

- View Medications

Invoke-RestMethod -Uri http://localhost:8090/medications
