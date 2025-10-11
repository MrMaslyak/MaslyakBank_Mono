# 💳 MaslyakBank_Mono — Документация

---

## 🏦 Описание

**MaslyakBank** — это модульное монолитное банковское приложение на Spring Boot. Оно симулирует основные функции банка: управление пользователями, счета и карты, финансовые транзакции — всё внутри одного репозитория. Проект разбит на отдельные модули, каждый отвечает за свою доменную область и общается с другими через REST API.

---

## ⚙️ Архитектура

- **MaslyakBank_Core**: 👤 Центральный сервис для управления пользователями, регистрацией, входом, профилями и админ-функциями (включая SUPER_ADMIN).
- **MaslyakBank_Account**: 💰 Управление счетами и картами — создание, выпуск, проверка баланса, переводы. Использует iban4j для IBAN.
- **MaslyakBank_Token**: 🔒 Сервис для аутентификации и управления токенами (JWT).
- **MaslyakBank_Transaction**: 🔄 Обработка финансовых транзакций, валидация, логирование истории.
- **MaslyakBank_SecurityKernel**, **MaslyakBank_Model**: 🛡️ Shared Kernel модули — общие компоненты безопасности (JwtAuthFilter, CustomUserDetailsService и др.), модели данных (JPA-сущности, DTO).
- **MaslyakBank_Infrastructure**: 🐳 Docker и база данных (Docker Compose для PostgreSQL, Redis, Liquibase миграций).

---

## 🚀 Ключевые возможности

- **Модульный монолит**: Простота монолита, чистое разделение по сервисам.
- **Управление пользователями и профилями**: Безопасная регистрация, вход, создание профиля.
- **Ролевой доступ**: 👤 USER, 🛡️ ADMIN, 👑 SUPER_ADMIN.
- **Счета и карты**: Создание счетов, выпуск дебетовых карт.
- **Транзакции**: Переводы карта-карта, валидация, логирование.
- **JWT авторизация**: Stateless, access/refresh токены.
- **Миграции базы**: Liquibase.
- **Контейнеризация**: Docker + Compose.
- **Автоматизированная инфраструктура**: PostgreSQL, Redis, Liquibase — всё поднимается через Docker Compose.
- **Валидация, аудит, логирование действий**.
- **Поддержка масштабирования и удобного сопровождения за счет единого репозитория**.

---

## 🛠️ Использованные технологии

- **Backend**: Java 21, Spring Boot 3
- **Database**: PostgreSQL
- **Caching**: Redis
- **Data Access**: Spring Data JPA, Hibernate
- **Security**: Spring Security, JSON Web Tokens (JWT)
- **Build Tool**: Apache Maven
- **Database Migration**: Liquibase
- **Containerization**: Docker, Docker Compose
- **Libraries**: Lombok, MapStruct, Iban4j

---

## 👥 Роли и доступ к эндпоинтам

| Модуль     | Эндпоинт                                                          | 👤 USER | 🛡️ ADMIN | 👑 SUPER_ADMIN |  
|------------|--------------------------------------------------------------------|---------|----------|---------------|
| CORE       | `/maslyakbank/user/registration`                                   | ✅      | ✅       | ❌            |           
| CORE       | `/maslyakbank/user/login`                                          | ✅      | ✅       | ❌            |           
| CORE       | `/maslyakbank/user/logout`                                         | ✅      | ✅       | ✅            |           
| CORE       | `/maslyakbank/admin/delete`                                        | ❌      | ✅       | ❌            |           
| CORE       | `/maslyakbank/profilemanagment/profile`                            | ✅      | ✅       | ❌            |           
| CORE       | `/actuator`                                                        | ❌      | ✅       | ❌            |           
| CORE       | `/maslyakbank/super-admin/grand-admin`                             | ❌      | ❌      | ✅            |           
| CORE       | `/maslyakbank/super-admin/revoke-admin`                            | ❌      | ❌      | ✅            |           
| ACCOUNT    | `/maslyakbank/accountmanagment/account/create`                     | ✅      | ✅       | ❌            |           
| ACCOUNT    | `/maslyakbank/accountmanagment/card/create`                        | ✅      | ✅       | ❌            |           
| ACCOUNT    | `/maslyakbank/accountmanagment/account/balance?cardNumber=`        | ✅      | ✅       | ❌            |           
| TOKEN      | `/maslyakbank/tokenmanagment/token/auth/create`                    | 🤖      | 🤖      | NONE          |           
| TOKEN      | `/maslyakbank/tokenmanagment/token/registration/create`            | 🤖      | 🤖      | NONE          |           
| TOKEN      | `/maslyakbank/tokenmanagment/token/refresh`                        | ✅      | ✅       | ✅            |           
| TOKEN      | `/maslyakbank/tokenmanagment/token/logout`                         | ✅      | ✅       | ✅            |           
| TOKEN      | `/maslyakbank/tokenmanagment/token/superadmin/create`              | NONE    | NONE     | 🤖            |           
| TRANSACTION| `/maslyakbank/transactionmanagment/transaction/transfer/card`      | ✅      | ✅       | ❌            |           

- ✅ — доступ разрешён
- ❌ — доступ запрещён
- 🤖 — используется системой (служебные запросы между сервисами)
- NONE — недоступен для этой роли

---

## 🧑‍💼 Описание ролей

- **👤 USER** — обычный пользователь, имеет доступ к регистрации, логину, транзакциям, просмотру баланса, созданию аккаунта/карты, обновлению токенов, логауту.
- **🛡️ ADMIN** — расширенные права, может дополнительно удалять пользователей, видеть технические эндпоинты (`/actuator`).
- **👑 SUPER_ADMIN** — максимальные права, может назначать/снимать роль админа.
- **🤖 SYSTEM** — служебные эндпоинты для взаимодействия между сервисами, не доступны пользователю напрямую.

---

## 📦 Пошаговый деплой

1. **Установить зависимости:**
    - JDK 21+
    - Apache Maven 3.9+
    - Docker и Docker Compose

2. **Клонировать репозиторий:**
    ```sh
    git clone https://github.com/MrMaslyak/MaslyakBank_Mono.git
    cd MaslyakBank_Mono
    ```

3. **Создать Docker-сеть:**
    ```sh
    docker network create MaslyakBankNetwork
    ```

4. **Запустить инфраструктуру (PostgreSQL, Redis, Liquibase миграции):**
    ```sh
    docker-compose -f MaslyakBank_Infrastructure/compose-infrastructure.yaml up -d --build
    ```

5. **Убедиться, что контейнер базы данных стал healthy:**
    ```sh
    docker ps
    ```

6. **Собрать все модули:**
    ```sh
    mvn clean install
    ```

7. **Запустить сервисы через Docker Compose (каждый сервис — свой файл):**
    ```sh
    # Core Service (User Management)
    docker-compose -f MaslyakBank_Core/compose-core.yaml up -d --build

    # Token Service (JWT Management)
    docker-compose -f MaslyakBank_Token/compose-token.yaml up -d --build

    # Account Service
    docker-compose -f MaslyakBank_Account/compose-account.yaml up -d --build

    # Transaction Service
    docker-compose -f MaslyakBank_Transaction/compose-transaction.yaml up -d --build
    ```

---

## 🌐 Локальные адреса сервисов

- **Core Service**: http://localhost:1200
- **Account Service**: http://localhost:1210
- **Transaction Service**: http://localhost:1220
- **Token Service**: http://localhost:1230

---

## 📝 Лицензия

Proprietary for Ilya Maslyanyi (Maslyak)
