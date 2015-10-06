# performance-test

Test de charge du site AirCorp.

Pour plus d'information voir [cd-infrastructure](https://github.com/snicaise/cd-infrastructure)

# Usage

Prérequis : maven 3 et java 8

Execution des tests :
```sh
mvn gatling:execute -Dgatling.simulationClass=web.BookingSimulation -Dbase_url=http://server:port
```
