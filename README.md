# Игра «Квадраты»
- **`squares-core`** — движок игры (правила, ИИ)
- **`squares-cli`** — консольное приложение (**Задание 1**)
- **`squares-service`** — REST-сервис и веб-страница (**Задание 2–3**)

---

## Требования

- Java **17+**
- Maven **3.8+**

---

## Старт

``` bash
 mvn clean install
```
---

## Задание 1 — Консольное приложение

``` bash
mvn -q -pl squares-cli -am package
java -jar squares/squares-cli/target/squares-cli-1.0.0-jar-with-dependencies.jar
```

## Команды для первого задания
```bash
HELP
GAME N, TYPE1 C1, TYPE2 C2 
MOVE X, Y
EXIT
```
---

## Задание 2 — REST-сервис

``` bash
mvn -q -pl squares-service -am spring-boot:run
```
Базовый URL: http://localhost:8080

## Пример запроса статуса:
```bash
curl -s -X POST http://localhost:8080/api/state \
  -H 'Content-Type: application/json' \
  -d '{"n":8,"turn":"W","white":[],"black":[]}'
```
## Пример запроса хода компьютера:
```bash
curl -s -X POST http://localhost:8080/api/move \
  -H 'Content-Type: application/json' \
  -d '{"n":8,"turn":"W","white":[],"black":[]}'
```

## 3 — Веб-приложение
```bash
mvn -q -pl squares-service -am spring-boot:run
```
Базовый URL: http://localhost:8080
