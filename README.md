# Projekt wymiany walut PLN <=> EUR

## Opis projektu
Aplikacja serwująca wymianę walut PLN/EUR oraz EUR/PLN przez REST API.

Zaprojektowana w architekture heksagonalnej używającej wzorca CQRS.

Baza danych H2 zapisująca do pliku (persistence).

## Technologie
Java 17, Spring Boot 3+, JPA (H2), Groovy, Spock

## Uruchomienie projektu - w terminalu
1. JDK 17 (Ustawione JAVA_HOME)
2. ./gradlew bootRun

lub uruchomić w ulubionym IDE wspierającym JVM :)

## Przykładowe Requesty
- [api-requests.http](requests/api-requests.http)

## Plusy rozwiązania
- Architektura heksagonalna oddzielająca logikę domenową od implementacji adapterów
- Przejrzystość dzięki CQRS
- Groovy/Spock w testach nadaje czytelności (można dodać tekst do sekcji given/when/then)

## Minusy rozwiązania
- **Największy problem** - rozdzielenie persystencji (Entity, repozytorium) od domeny skomplikowało utrzymanie transakcji (więcej w sekcji [Problem transakcyjności i skalowalności](#problem-transakcyjności-i-skalowalności))
- Architektura heksagonalna i CQRS wygenerowały dużo kodu - overengineering dla tak prostego rozwiązania

## Problem transakcyjności i skalowalności + rozwiązania
Wymiana walut powinna być odporna na powtórzone zapytania, wiele jednoczesnych wymian dla tego samego konta, a sam serwis być stateless (dla skalowalności).

Rozwiązaniem na powyższe byłoby zastosowanie indempotentości zapytań oraz jednego z rozwiązań:
1. Optimistic lock z @Version + ewentualne retry - zakładamy, że użytkownik rzadko dubluje zapytania dla własnego konta
2. Pesimistic lock SELECT FOR UPDATE

Niestety żadne nie może być wprowadzone przez rozdział domeny od warstwy persystencji gdzie w wymianie tracimy sesję EntityManager.

JPA zostało wydzielone do osobnego adaptera (persistence_adapter), aby domena nie wiedziała nic o implementacji persystencji do bazy.
W tym celu moduł mapuje encje `AccountEntity` na DTO `AccountDetailsDto` przy zwracaniu konta i tak samo przyjmuje DTO przy zapisie do bazy - 
**tym samym tracąc sesję EnityManager**.
Wymiana walut odbywa się w domenie w `ExchangeCurrencyService` w metodzie `handle`, która jest adnotowana `@Transactional`.
Ma ona kilka kroków - 1. walidacja danych, 2. pobranie Account DTO, 3. obliczenia, 4. sprawdzenie czy są wymagane środki i 5. zapis zmienionego DTO.
Wszystko pomimo adnotacji `@Transactional` nie działa w transakcji, bo między punktem 2, a 5 tracimy sesję.
Gdy my upewnimy się w punkcie 4, że mamy środki - inny proces może już zapisać przewalutowanie i tych środków może braknąć.

Jakimś rozwiązaniem jest @Query z pełnym UPDATE oraz sprawdzaniem wersji według optimistic lock ale wymaga to
ręcznego pisania i utrzymywania zapytania @Query. To daje sprawdzenie wersji za jednym zamachem z update.
Wszelkie inne opcje w kodzie, bez sesji EntityManager, nadal zostawiają lukę w czasie na zmianę wersji rekordu
przez inny proces.

Stąd warto zastanowić się czy nie przenieść warstwy persistence do domeny.

## Możliwe usprawnienia
### Istotne
1. Rozwiązanie problemu z transakcyjnością i skalowalnością - jednym z prostszych zasugerowanych rozwiązań + idempotencja requestów
2. Poprawne zaokrąglanie do X miejsca po przecinku BigDecimal - stanu konta, przewalutowania 
3. Generyczne rozwiązanie, aby z łatwością dodąć nowo obsługiwaną walutę - AccountEntity np. z Map<Currency, BigDecimal>, a dodanie nowo obsługiwanej byłoby tylko w enum Currency 
4. Bardziej domenowe podejście - opakować BigDecimal w klasę z potrzebnymi metodami itp. 
5. Cache dla pobranego kursu z NBP - co X czasu (tyle co ile się zmienia)
6. Większe pokrycie testami - jednostkowe, integracyjne, TestContainers

### Warte uwagi
1. Account ID z Long na UUID - ciężej przewidzieć jego wartość niż inkrementowanego Long (wybrane dla ułatwienia testowania)
2. Spring Security, Uwierzytelnianie/Autoryzacja, JWT
3. Swagger do dokumentacji API 
4. Liquibase do zarządzania schematem/migracją bazy
4. Użyć tabeli C z kupno/sprzedaż zamiast tabeli A z NBP (wybrana dla ułatwienia)
5. Prawdziwy rozdział heksagonalny - package jak domain, api, persistence_adapter itd. jako moduły gradle (nie widzą swoich bibliotek i klas)
6. Dodać moduł h2_adapter z dynamiczną konfiguracją bazy przez klasę @Configuration zamiast w properties - oddzielenie konfiguracji bazy danych od properties. Można @Profile włączyć h2, a innym razem inną bazę 
7. Więcej logów 
8. Monitoring, alerting 
9. Vault dla sekreteów, haseł
10. Opis custom zmiennych w README
11. Konteneryzacja rozwiązania np. przez Docker 
12. CI/CD