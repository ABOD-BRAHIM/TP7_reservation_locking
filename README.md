## Gestion de la concurrence et du verrouillage optimiste

### 1. Simulation de la concurrence

La classe `ConcurrentReservationSimulator` simule deux transactions concurrentes qui tentent de modifier la même réservation. Le verrouillage optimiste permet de détecter le conflit de modification.

![ConcurrentReservationSimulator](screnshot/Capture%20d%27%C3%A9cran%202026-10-18%20151345.png)

### 2. Gestion des conflits avec Retry

La classe `ConcurrentReservationWithRetry` met en œuvre un mécanisme de nouvelle tentative automatique (*retry*) lorsqu'un conflit de concurrence est détecté.

![ConcurrentReservationWithRetry](screnshot/Capture%20d%27%C3%A9cran%202026-10-18%20151353.png)

### 3. Gestionnaire de nouvelles tentatives

La classe `ReservationRetryHandler` gère les exceptions de verrouillage optimiste en rechargeant la dernière version de la réservation avant de retenter la modification.

![ReservationRetryHandler](screnshot/Capture%20d%27%C3%A9cran%202026-10-18%20151402.png)

### 4. Gestion des transactions

La classe `ServiceImpl` implémente les opérations de création et de modification des réservations en gérant explicitement les transactions, les validations (*commit*) et les annulations (*rollback*) en cas d'erreur.

![ServiceImpl](screnshot/Capture%20d%27%C3%A9cran%202026-10-18%20151411.png)

### 5. Résultat de la simulation sans Retry

La console montre qu'une première transaction réussit, tandis que la seconde échoue en raison d'un conflit de version détecté par le mécanisme de verrouillage optimiste.

![Résultat de la simulation sans Retry](screnshot/Capture%20d%27%C3%A9cran%202026-10-18%20151846.png)

### 6. Résultat de la simulation avec Retry

La console montre que le mécanisme de nouvelle tentative permet de recharger les données actualisées et de réussir la modification après la résolution du conflit.

![Résultat de la simulation avec Retry](screnshot/Capture%20d%27%C3%A9cran%202026-10-18%20151858.png)