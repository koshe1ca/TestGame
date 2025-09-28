# Игра «Квадраты»
- **`squares-core`** — движок игры (правила, ИИ)
- **`squares-cli`** — консольное приложение (**Задание 1**)
- **`squares-service`** — REST-сервис и веб-страница (**Задание 2–3**)

---

## Требования

- Java **17+**
- Maven **3.8+**
---

Все команды выполняются из папки squares
```bash
cd squares
```

## Сборка проекта

```bash
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
Пример с человеком против компьютера:
```bash
HELP
GAME 8, user W, comp B
MOVE 3, 4
EXIT
```
---

## Задание 2 — REST-сервис
Сборка и запуск 
``` bash
mvn -q -pl squares-service -am clean package
java -jar .\squares-service\target\squares-service-1.0.0.jar
```
Пример запроса статуса:
```bash
curl.exe -X POST http://localhost:8080/api/state `
  -H "Content-Type: application/json" `
  -d "{\"n\":8,\"turn\":\"W\",\"white\":[],\"black\":[]}"
```
Пример запроса хода компьютера:
```bash
curl.exe -X POST http://localhost:8080/api/move `
  -H "Content-Type: application/json" `
  -d "{\"n\":8,\"turn\":\"W\",\"white\":[],\"black\":[]}"
```

## Задание 3 — Веб-приложение
Сборка и запуск
```bash
mvn -q -pl squares-service -am clean package
java -jar .\squares-service\target\squares-service-1.0.0.jar
```
Базовый URL: http://localhost:8080
