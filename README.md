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

Откройте командную строку и выполните:
```bash
mvn -version
java -version
```

## Запуск бэкенда:
mvn spring-boot:run

## Запуск фронтенда:
npm run serve
