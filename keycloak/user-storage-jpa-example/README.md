Провайдер для локального хранилища пользователей Keycloak
===================================================

Этот проект построен на примере провайдера пользователей Keycloak (версия 3.4.3) использующего EJB и JPA.
(см. проект исходного примера - https://github.com/keycloak/keycloak/tree/master/examples/providers/user-storage-jpa).

Отличия от оригинального примера:

* Провайдер предоставляет пользователей со всеми ролями.
(см. описание ролей - http://www.keycloak.org/docs/3.3/server_admin/topics/roles.html)
* Провайдер работает с типом Long для Id пользователя.
* Провайдер предоставляет Id атрибут пользователя.

### Настройка

1) Настройте `UserEntity` на табличку пользователей Вашей БД.

2) Найтройте `driver` и `XA datasource` БД, которой Вы хотите использовать на стороне сервера Keycloak.
(см. Relational Database Setup - http://www.keycloak.org/docs/1.9/server_installation_guide/topics/database.html)

Пример конфигурации `XA datasource`: 

```
<xa-datasource jndi-name="java:jboss/datasources/ExampleXADS" pool-name="ExampleXADS" enabled="true" use-java-context="true">
    <xa-datasource-property name="URL">
        jdbc:postgresql://localhost:5432/postgres
    </xa-datasource-property>
    <xa-datasource-property name="DatabaseName">
        postgres
    </xa-datasource-property>
    <driver>postgresql</driver>
    <xa-pool>
        <max-pool-size>20</max-pool-size>
    </xa-pool>
    <security>
        <user-name>admin</user-name>
        <password>admin</password>
    </security>
</xa-datasource>
<drivers>
    ...
    <driver name="postgresql" module="org.postgresql">
        <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
    </driver>
</drivers>

```

3) В `resources/META-INF/persistence.xml` настройте JPA проекта на `XA datasource`, который Вы задали в Keycloak.

4) *Если Вы используете PostgreSQL убедитесь, что в Вашей БД выставлен параметр `max_prepared_transactions`.

### Деплой проекта

Пусть Keycloak запущен локально, как `standalone` на порту `8080`,
тогда для сборки проекта и деплоя в Keycloak достаточно команды:

    mvn clean install wildfly:deploy

Теперь, если Вы войдёте в Keycloak, то во вкладке `User Federation` Вы обнаружите данный провайдер для подключения.
Добавте провайдер и сохраните, теперь любой новый пользователь будет сохранён в БД провайдера.

