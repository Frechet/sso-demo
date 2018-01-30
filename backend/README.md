Бекенд авторизации и регистрации
=================================================== 

Модуль пердоставлят REST API для регистрации и авторизации пользователей.
Описание REST API сервиса предоставляется инструментом Swagger 2.0 [https://swagger.io/].

Описание
----------

Сборка и настройка проекта
-----------

* Сконфигурируйте доступ к локальной БД для хранение регистрируемых пользователей в `application.properties#spring.datasource.*`

* Чтобы активировать Keycloak SSO используйте ``--spring.profiles.active=keycloak-sso`` аргумент командной строки
или ``spring.profiles.active`` конфигурацию в ``application.properties`` файле.

    Настройки клиента `backend` для Keycloak выставляются в `application.properties` файле параметрами`keycloak.*`,
    а доступность ресурсов по ролям настройками Spring Security в классе `KeycloakConfig`.
    
* Чтобы активировать CAS SSO  используйте ``--spring.profiles.active=cas-sso`` аргумент командной строки
или ``spring.profiles.active`` конфигурацию в ``application.properties`` файле.

    Настройки клиента `backend` для CAS SSO выставляются в `application.properties` файле параметрами `security.oauth2.*`,
    а доступность ресурсов по ролям настройками Spring Security в классе `CasConfig`.

* Проект предоставлят два профиля для сборки проекта `spring` - Spring Boot jar и `tomcat` - Tomcat war.