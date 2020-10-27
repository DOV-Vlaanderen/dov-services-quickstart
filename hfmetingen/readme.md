# Demo applicatie voor het gebruik van DOV-services

## Toegang tot de DOV services
Het administratieve proces om gebruik te kunnen maken van de DOV-services bestaat uit 2 stappen:

    * Aanvraag toegang DOV-services
    * Aanmaken certificaat
Meer informatie is terug te vinden op [DOV confluence](https://www.milieuinfo.be/confluence/display/DDOV/Synchronisatie+-+gebruik+DOV-webservices).


## Case 2: Hoog frequente metingen
De klasse **hfmetingen/src/main/java/be/vlaanderen/dov/services/DemoApplication** is een kleine uitvoerbare spring boot applicatie die toont hoe kan gecommuniceerd worden met de DOV-hoog frequente metingen API:
  * Healthcheck
  * Verkrijgen van een lijst van sensoren aan een instrument
  * Upload sensor data
  * Verwijderen sensor data
  * Download data aan een sensor
  * Download data aan een filter (meetreeksdata)
  
Een uitgebreidere beschrijving van de services is te vinden op de [DOV confluence](https://www.milieuinfo.be/confluence/display/DDOV/Case+2%3A+Hoog+frequente+metingen)
