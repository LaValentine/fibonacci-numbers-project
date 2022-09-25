# Описание
Три микросервиса

Первый микросервис умеет выдавать последовательность первых 50 чисел Фибоначчи (каждые 100мс по одному числу).

Второй микросервис умеет обращаться к первому, выбирать какой-либо диапазон чисел, считать их сумму и кешировать результат в течение 5 секунд (не более 20 запросов)

Третий микросервис собирает логи первых двух

## Технологии
* Java 17
* RSocket
* Spring WebFlux
* Caffeine Cache

## Запуск приложения
ШАГ #1 Создание образа с окружением для запуска программы

Для запуска приложения необходимо установить maven 3.6.3 и openjdk 17

Dockerfile данного образа:

```
FROM ubuntu:18.04   # образ будет создан на основе образа ubuntu:18.04

ARG MAVEN_VERSION=3.6.3                                                             # Переменная хранит версию maven
ARG USER_HOME_DIR="/root"                                                           # Переменная хранит корневую директорию пользователя
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries      # Адрес ссылки, с которй будет скачен maven

RUN apt-get update \                                    # Перед установкой нужных компонентов необходимо обновить репозитории операционной системы
    && apt-get install -y curl \                        # Устанавливаем утилиту curl, она необходима для скачивания maven
    && apt-get install -y openjdk-17-jre-headless       # Устанавливаем Java

RUN mkdir -p /usr/share/maven \                                                                         # Создаем директории, для maven  
    && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \    # Скачиваем архив с maven
    && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \                     # Распаковываем архив с maven
    && rm -f /tmp/apache-maven.tar.gz \                                                                 # Удаляем архив с maven (он не нужен, так как мы уже его распаковали)
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn                                                      # Копируем содержимое в директорию mvn (ln - команда, которая позволяет размещать один и тот же файл в нескольких директориях)

ENV MAVEN_HOME /usr/share/maven                     # Устанавливаем переменную окружения, хранящую домашнюю директорию maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"               # Устанавливаем переменную окружения, хранящую директорию с репозиториями maven

ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64    # Устанавливаем переменную окружения, хранящую домашнюю директорию Java
```

Необходимо выполнить сборку образа

```
docker build -t group-1/maven-jdk ./maven-jdk
```

ШАГ #2 Создание образов первого и второго микросервисов

Для работы сборщика логов в первом и втором микросервисе необходимо добавить файл rsyslog-client.conf с содержимым:

```
*.* @@172.18.0.2:514                            # ip адрес контейнера - сборщика логов

module(load="imfile" PollingInterval="10")      # Устанавливаем частоту обновления файла (в нашем случае 10 сек)

input(type="imfile"                             # Задаем параметры файла логов
    File="/logging.log"
    Tag="handler-logging"
    Severity="info")
do
```
*В нашем случае и для первого и для второго миросервиса файл будет одинаковым


Оба микросервиса разработаны на основе Java 17 и Maven 3.6.3, поэтому инструкции для их создания будут одинаковые

Dockerfile образов:

```

FROM group-1/maven-jdk      # Образ будет создан на основе образа group-1/maven-jdk

COPY src /home/app/src      # Копируем папку src
COPY pom.xml /home/app      # Копируем pom.xml

RUN mvn -f /home/app/pom.xml package -DskipTests                # Выполняем сборку проекта

ENTRYPOINT java -jar /home/app/target/*.jar                     # После запуска контейнера необходимо будет запустить проект

COPY rsyslog-client.conf /etc/rsyslog.d/rsyslog-client.conf     # Копируем конфигурацию для сборщика логов

RUN apt-get update && apt-get install -y rsyslog                # Обновляем репозитории операционной системы и устанавливаем утилиту rsyslog

```

Далее необходимо создать образ для каждого микросервиса

```
docker build -t group-1/handler-fibonacci-numbers ./handler-fibonacci-numbers
```

```
docker build -t group-1/sender-fibonacci-numbers ./sender-fibonacci-numbers
```

ШАГ #3 Создание образа сборщика логов

Для работы сборщика логов в контейнер необходимо создать файл rsyslog-service.conf со следующим содержимым

```
$ModLoad imudp
$UDPServerRun 514

$ModLoad imtcp
$InputTCPServerRun 514


$template RemoteLogs,"/var/log/rsyslog/%HOSTNAME%/logging.log"

*.* ?RemoteLogs
& ~
```

Dockerfile для данного образа

```
FROM ubuntu:18.04                                           # Образ будет создан на основе образа ubuntu:18.04 
RUN apt-get update && apt-get install -y rsyslog            # Обновление репозиториев операционной системы и установка утилиты rsyslog
ADD rsyslog-service.conf /etc/rsyslog.d/rsyslog-client.conf # Добавление файла конфигурации
RUN service rsyslog start                                   # Запуск rsyslog
```

Далее необходимо выполнить сборку образа

```
docker build -t group-1/logging-service ./logging-service
```

ШАГ #4 Запуск контейнеров

Для начала создадим сеть внутри которой будут работать контейнеры

```
docker network create --subnet=172.18.0.0/24 fibonacci-numbers
```

Запустим контейнер сборщик логов, с указанием ip адреса, который мы прописали в файлах rsyslog-client.conf

```
docker run --net fibonacci-numbers --ip 172.18.0.2 --name logging-service group-1/logging-service
```

Запускаем первый и второй контейнеры

```
docker run --net fibonacci-numbers --name sender-fibonacci-numbers -p 7000:7000 group-1/sender-fibonacci-numbers
```

```
docker run --net fibonacci-numbers --name handler-fibonacci-numbers -p 8080:8080 -e RSOCKET_SEFRVER_HOST=sender-fibonacci-numbers group-1/shandler-fibonacci-numbers
```
ШАГ #5 Запускаем утилиту rsyslog

Подключаемся к терминалу первого контейнера и запускаем rsyslog

```
docker exic -it sender-fibonacci-numbers
```

```
service rsyslog start
```

Аналогично со вторым контейнером

```
docker exic -it handler-fibonacci-numbers
```

```
service rsyslog start
```

Подключаемся с контейнеру сборщику логов и перезапускаем rsyslog

```
docker attach logging-service
```

```
service rsyslog restart
```
### После выполнения всех шагов в контейнере logging-service, папке /var/log/rsyslog/ мы можем найти файлы логов первого и второго сервисов

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