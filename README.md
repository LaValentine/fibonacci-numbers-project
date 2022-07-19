# Описание
Два микросервиса

Первый микросервис умеет выдавать последовательность первых 50 чисел Фибоначчи (каждые 100мс по одному числу).

Второй микросервис умеет обращаться к первому, выбирать какой-либо диапазон чисел, считать их сумму и кешировать результат в течение 5 секунд (не более 20 запросов)

## Технологии
* Java 17
* RSocket
* Spring WebFlux
* Caffeine Cache

## Запуск приложения
ШАГ #1

Установите переменные окружения или же оставьте значения по умолчанию
  ```
  SERVER_PORT=8080
  RSOCKET_SERVER_HOST=localhost
  RSOCKET_SERVER_PORT=7000
  ```
ШАГ #2

  ```
  mvn  -f ./sender-fibonacci-numbers package -DskipTests
  ```
  ```
  mvn  -f ./handler-fibonacci-numbers package -DskipTests
  ```
ШАГ #3

  ```
  java -jar ./sender-fibonacci-numbers/target/sender-fibonacci-numbers-0.0.1-SNAPSHOT.jar
  ```
  ```
  java -jar ./handler-fibonacci-numbers/target/handler-fibonacci-numbers-0.0.1-SNAPSHOT.jar
  ```
## Запуск приложения с использованием Docker

ШАГ #1

  ```
  docker build -t lavalentine/sender-fibonacci-numbers -f ./sender-fibonacci-numbers .
  ```
  ```
  docker build -t lavalentine/handler-fibonacci-numbers -f ./handler-fibonacci-numbers .
  ```
ШАГ #2

  ```
  docker run --name sender-fibonacci-numbers -p 7000:7000 lavalentine/sender-fibonacci-numbers
  ```
  ```
  docker run --name handler-fibonacci-numbers -p 8080:8080 -e RSOCKET_SERVER_HOST=host.docker.internal lavalentine/handler-fibonacci-numbers
  ```
## Или
ШАГ #1

  ```
  docker compose up
  ```

## Документация API
### GET  `/api/fibonacci-numbers/sum`
#### Получение первых 50 чисел Фибоначчи
> Ответ
>   ```
>   data:{"fibonacci-number":1,"index":1}
>
>   data:{"fibonacci-number":1,"index":2}
>
>   data:{"fibonacci-number":2,"index":3}
> 
>   data:{"fibonacci-number":3,"index":4}
>
>   data:{"fibonacci-number":5,"index":5}
>   
>   data:{"fibonacci-number":8,"index":6}
>
>   data:{"fibonacci-number":13,"index":7}
>
>   data:{"fibonacci-number":21,"index":8}
>
>   data:{"fibonacci-number":34,"index":9}
>
>   data:{"fibonacci-number":55,"index":10}
>
>   data:{"fibonacci-number":89,"index":11}
>
>   data:{"fibonacci-number":144,"index":12}
>
>   data:{"fibonacci-number":233,"index":13}
>
>   data:{"fibonacci-number":377,"index":14}
>   
>   data:{"fibonacci-number":610,"index":15}
>   
>   data:{"fibonacci-number":987,"index":16}
>
>   data:{"fibonacci-number":1597,"index":17}
>
>   data:{"fibonacci-number":2584,"index":18}
>
>   data:{"fibonacci-number":4181,"index":19}
>   
>   data:{"fibonacci-number":6765,"index":20}
>
>   data:{"fibonacci-number":10946,"index":21}
>   
>   data:{"fibonacci-number":17711,"index":22}
>   
>   data:{"fibonacci-number":28657,"index":23}
>   
>   data:{"fibonacci-number":46368,"index":24}
>   
>   data:{"fibonacci-number":75025,"index":25}
>   
>   data:{"fibonacci-number":121393,"index":26}
>
>   data:{"fibonacci-number":196418,"index":27}
>
>   data:{"fibonacci-number":317811,"index":28}
>
>   data:{"fibonacci-number":514229,"index":29}
>
>   data:{"fibonacci-number":832040,"index":30}
>
>   data:{"fibonacci-number":1346269,"index":31}
>   
>   data:{"fibonacci-number":2178309,"index":32}
>   
>   data:{"fibonacci-number":3524578,"index":33}
>
>   data:{"fibonacci-number":5702887,"index":34}
>   
>   data:{"fibonacci-number":9227465,"index":35}
>   
>   data:{"fibonacci-number":14930352,"index":36}
>   
>   data:{"fibonacci-number":24157817,"index":37}
>   
>   data:{"fibonacci-number":39088169,"index":38}
>
>   data:{"fibonacci-number":63245986,"index":39}
>
>   data:{"fibonacci-number":102334155,"index":40}
>
>   data:{"fibonacci-number":165580141,"index":41}
>
>   data:{"fibonacci-number":267914296,"index":42}
>
>   data:{"fibonacci-number":433494437,"index":43}
>
>   data:{"fibonacci-number":701408733,"index":44}
>
>   data:{"fibonacci-number":1134903170,"index":45}
>
>   data:{"fibonacci-number":1836311903,"index":46}
>
>   data:{"fibonacci-number":2971215073,"index":47}
>
>   data:{"fibonacci-number":4807526976,"index":48}
>
>   data:{"fibonacci-number":7778742049,"index":49}
>
>   data:{"fibonacci-number":12586269025,"index":50} 
>   ```  

### GET  `/api/fibonacci-numbers/sum`
#### Получение суммы чисел Фибоначчи в диапазоне от `min-value` до `max-value`
> Параметры запроса:
>   ```
>   max-value: long
>   min-value: long
>   ```
> Ответ
>   ```
>   {
>       "fibonacci-numbers-sum": long,
>       "min-value": long,
>       "max-value": long
>   }
>   ```

## Примеры запросов через терминал 
*** Примеры составлены с учетом, что сервис запущен на порте 8080
>  Запрос
>  ```
>  curl -X GET "http://localhost:8080/api/fibonacci-numbers/sum?max-value=100&min-value=1"
>  ```
>  Ответ
>   ```
>  {
>       "fibonacci-numbers-sum":232,
>       "min-value":1,
>       "max-value":100
>   }
>   ```

> Запрос
>   ```
>   curl -X GET "http://localhost:8080/api/fibonacci-numbers/sum?max-value=100&min-value=1000"
>   ```
> Ответ
>   ```
>  The min parameter is greater than the max parameter
>   ```

> Запрос
>   ```
>   curl -X GET "http://localhost:8080/api/fibonacci-numbers"
>   ```
> Ответ
>   ```
>   data:{"fibonacci-number":1,"index":1}
>
>   data:{"fibonacci-number":1,"index":2}
>
>   data:{"fibonacci-number":2,"index":3}
> 
>   data:{"fibonacci-number":3,"index":4}
>
>   data:{"fibonacci-number":5,"index":5}
>   
>   data:{"fibonacci-number":8,"index":6}
>
>   data:{"fibonacci-number":13,"index":7}
>
>   data:{"fibonacci-number":21,"index":8}
>
>   data:{"fibonacci-number":34,"index":9}
>
>   data:{"fibonacci-number":55,"index":10}
>
>   data:{"fibonacci-number":89,"index":11}
>
>   data:{"fibonacci-number":144,"index":12}
>
>   data:{"fibonacci-number":233,"index":13}
>
>   data:{"fibonacci-number":377,"index":14}
>   
>   data:{"fibonacci-number":610,"index":15}
>   
>   data:{"fibonacci-number":987,"index":16}
>
>   data:{"fibonacci-number":1597,"index":17}
>
>   data:{"fibonacci-number":2584,"index":18}
>
>   data:{"fibonacci-number":4181,"index":19}
>   
>   data:{"fibonacci-number":6765,"index":20}
>
>   data:{"fibonacci-number":10946,"index":21}
>   
>   data:{"fibonacci-number":17711,"index":22}
>   
>   data:{"fibonacci-number":28657,"index":23}
>   
>   data:{"fibonacci-number":46368,"index":24}
>   
>   data:{"fibonacci-number":75025,"index":25}
>   
>   data:{"fibonacci-number":121393,"index":26}
>
>   data:{"fibonacci-number":196418,"index":27}
>
>   data:{"fibonacci-number":317811,"index":28}
>
>   data:{"fibonacci-number":514229,"index":29}
>
>   data:{"fibonacci-number":832040,"index":30}
>
>   data:{"fibonacci-number":1346269,"index":31}
>   
>   data:{"fibonacci-number":2178309,"index":32}
>   
>   data:{"fibonacci-number":3524578,"index":33}
>
>   data:{"fibonacci-number":5702887,"index":34}
>   
>   data:{"fibonacci-number":9227465,"index":35}
>   
>   data:{"fibonacci-number":14930352,"index":36}
>   
>   data:{"fibonacci-number":24157817,"index":37}
>   
>   data:{"fibonacci-number":39088169,"index":38}
>
>   data:{"fibonacci-number":63245986,"index":39}
>
>   data:{"fibonacci-number":102334155,"index":40}
>
>   data:{"fibonacci-number":165580141,"index":41}
>
>   data:{"fibonacci-number":267914296,"index":42}
>
>   data:{"fibonacci-number":433494437,"index":43}
>
>   data:{"fibonacci-number":701408733,"index":44}
>
>   data:{"fibonacci-number":1134903170,"index":45}
>
>   data:{"fibonacci-number":1836311903,"index":46}
>
>   data:{"fibonacci-number":2971215073,"index":47}
>
>   data:{"fibonacci-number":4807526976,"index":48}
>
>   data:{"fibonacci-number":7778742049,"index":49}
>
>   data:{"fibonacci-number":12586269025,"index":50} 
>  ```