# ПАНЕЛЬ НАВИГАЦИИ
- [Быстрый старт](#быстрый-старт)
- [Особенности проекта](#особенности)
- [Как можно будет ознакомиться с документацией API](#документация-api-)
- [Решение возможных проблем](#решение-проблем-)
- [Начальные пользователи](#начальные-пользователи-)

# Backend Spring Boot + Maven

### Нужно будет скачать:
- [Maven](https://maven.apache.org/download.cgi)
- [Spring Boot](https://start.spring.io/)
- [Java](https://www.oracle.com/java/technologies/downloads/#jdk21-windows) **(Если нету)**

### Что нужно выбрать при установке Spring Boot:
- **Project:** Maven  
- **Language:** Java  
- **Spring Boot:** 3.4.2  
- **Project Metadata:** На ваше усмотрение  
- **Packing:** Jar  
- **Java:** 21

# Инструкция по установке и настройке Maven и Java

## 1. Установка Apache Maven

1. Скачайте Maven с [официального сайта](https://maven.apache.org/download.cgi) (версия 3.9.9).
2. Распакуйте архив в выбранную папку, например:  
   `C:\Program Files\apache-maven-3.9.9`.
3. Добавьте путь к Maven в переменные среды:
   - Откройте **Параметры системы** → **Переменные среды**.
   - В разделе **Системные переменные** выберите `Path` → **Изменить** → **Создать**.
   - Вставьте путь к папке `bin` Maven:  
     `C:\Program Files\apache-maven-3.9.9\bin`.
   - Нажмите **ОК** во всех окнах.

## 2. Настройка переменной JAVA_HOME

1. Убедитесь, что установлен **JDK 21** (скачайте с [официального сайта](https://www.oracle.com/java/technologies/downloads/)).
2. Проверьте путь к JDK, например:  
   `C:\Program Files\Java\jdk-21`.
3. Настройте переменную `JAVA_HOME`:
   - В разделе **Системные переменные** нажмите **Создать**.
   - Введите:
     - **Имя переменной**: `JAVA_HOME`
     - **Значение переменной**: `C:\Program Files\Java\jdk-21`
   - Нажмите **ОК**.
4. Обновите `Path`:
   - Выберите `Path` → **Изменить** → **Создать**.
   - Добавьте: `%JAVA_HOME%\bin`.

Проверьте установку:
```bash
mvn -version
java -version
```

# Transport Marketplace 🚚

Полнофункциональное приложение для управления транспортными услугами на основе Spring Boot и Vue.js с использованием Docker.

---

## Особенности
- Аутентификация через JWT
- Ролевая модель доступа (Админ/Пользователь)
- H2 Database с файловым хранилищем
- Документация API через Swagger
- Docker-контейнеризация

---

## Требования
- Docker 20.10+
- Docker Compose 2.20+
- JDK 21
- Maven 3.9.9

# Быстрый старт

## Установка через Docker🐳
- Клонируйте репозеторий:
```
git clone https://github.com/ваш-логин/transport-marketplace.git
cd transport-marketplace
```
- Запустите проект:
```
docker-compose up --build
```
- Сервисы будут доступны:
  - Фронтенд: http://localhost
  - Бэкенд: http://localhost:8080
  - Swagger: http://localhost:8080/swagger-ui.html
  - H2 Console: http://localhost:8080/h2-console
### Основные команды:
| Команда                          | Описание                                 |
|----------------------------------|------------------------------------------|
| `docker-compose down -v`         | Полная остановка с удалением данных      |
| `docker-compose logs -f backend` | Просмотр логов бэкенда                    |


### Ручной запуск
```
# Бэкенд
mvn spring-boot:run

# Фронтенд (из папки client)
npm install
npm run serve
```

# Документация API 📚
Доступна через Swagger UI:
```
http://localhost:8080/swagger-ui.html
```
### Авторизация:
- Используйте логины:
    - Админ: admin/secure_admin
    - Пользователь: user/user_password
- Добавьте токен в заголовки запросов:
    ```
    Authorization: Bearer <ваш_токен>
    ```

# Решение проблем 🚨
Сборка Docker завершается с ошибкой:
## Частые ошибки:
1. Сборка Docker завершается с ошибкой:
    ```
    docker system prune -a  # Очистка кэша
    rm -rf client/node_modules client/package-lock.json
    ```
2. Проблемы с H2 Database:
 - Проверьте настройки подключения в H2 Console:
     ```
    JDBC URL: jdbc:h2:file:/app/data/transport_db
    User: root
    Password: root
    ```
# Начальные пользователи 👥
## Автоматически создаются при первом запуске:
 | Логин   | Пароль          |
 |---------|-----------------|
 | `admin` | `secure_admin`  |
 | `user`  | `user_password` |



    
