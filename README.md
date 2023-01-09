# О проекте:

Backend сервис для приложения «Мобильный хоспис»

**Технологии:** Java, Spring Boot, Postgres

# Первичная установка

1. Скачать проект с гитхаба https://github.com/fmh-charity/fmh-backend
2. Обновить мавен зависимости
3. Добавить в конфигурацию идеи environment variables DB_PORT=5400;DB_HOST=localhost;DB_USER=postgres;DB_PASS=123;DB_NAME=FMH_DB; SWAGGER_HOST=; DOCUMENTS_STATIC_PATH=;
4. Установить докер(десктоп версию под Win or Mac)
5. запустить локальный файл компоуз для поднятия бд либо через idea либо в терминале `docker-compose -f docker-compose-env-only.yml up`
6. под профилем dev сделать clean package
7. запустить Application
8. Прогнать скрипт в бд R__init_users.sql для создания тестовых пользователей


#Тестирование эндпоинтов:
##  через scratch:

POST http://localhost:8080/fmh/authentication/login
Content-Type: application/json

```
{
  "login": "login1",
  "password": "password1"
}
```

## через cucumber:

1. под профилем cucumber в модуле cucumber-tests сделать clean package
2. cucumber-tests/src/test/java/cucumber/TestRunner.java запустить тесты Run Tests, убедиться, что все прошли без ошибок.


# Локальный запуск:

## Вариант 1, backend сервис и окружение в докере

1. Собрать docker image: `docker build -t fmh_back:1.0`  
2. Выполнить в корне `docker-compose up`
3. Перейти в Swagger - `http://localhost:8080/fmh/swagger-ui/index.html` 

## Вариант 2, backend запустить локально, а окружение в докере

1. Выполнить в корне `docker-compose -f docker-compose-env-only.yml up`
2. Установить переменные окружения (env vars): `DB_PORT=5400;DB_HOST=localhost;DB_USER=postgres;DB_PASS=123`
3. Запустить backend сервис
4. Перейти в Swagger - `http://localhost:8080/fmh/swagger-ui/index.html`

# Проверка локально всех тестов в модуле fmh-backend
1. В папке _.run_ находится файл _AllTests.run.xml_, откройте его и нажмите **Open Run/Debug Configurations**
2. В открывшемся диалоговом окне поправьте необходимые поля (предположительно версию java) и сохраните конфигурацию.
3. Запускаете сохраненную конфигурацию AllTests.

Для тестов в БД должна быть чистой с выполненным _R__initialization_data.sql_:
1. В локальной БД удалить все таблицы из схемы public, используя cascade.
2. Запустить Application с профилем dev, чтоб прошли все flyway-миграции.
3. Выполнить все скрипты из файла _src/main/resources/db/migration/test/R__initialization_data.sql_.
