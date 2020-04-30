Демонстрация работы SSO/CAS системы
===================================

Данный прототип призван рассмотреть пользовательские
случаи взаимодействия микросервисной архитектуры с системой управления
аутентификацией и доступом SSO/CAS - **Keycloak** (см. http://www.keycloak.org) и
**CAS Enterprise Single Sign-On** (https://github.com/apereo/cas). Подробное описание см. [doc/demo.adoc](doc/demo.adoc).

Рассмотрены следующие пользовательские случаи:

1) Фронтенд приложение (`frontend-registration`) имеет доступ к специализированным ресурсам Бэкенда (`backend`).
Данный случай реализован припиской особого пользователя для Фронтенда, через которого приложение ходит в Бэкенд.

2) Созданные новые пользователи на Бэкенде поддерживаются системой SSO/CAS.

3) Другое фронтенд приложение отличное от первого (`frontend-accounting`) предоставляет форму авторизации
для пользователей с доступом к ресурсам Бэкенда (`backend`).

Описание модулей
-----------

* ``cas``: war overlay сервер CAS Enterprise Single Sign-On
* ``keycloak``: описание конфигурации сервера Keycloak и плагины для целей демонстрации
* ``backend``: модуль регистрации, предоставляет защищённый ресурс создания новых пользователей и получение данных по имеющимся
* ``frontend-registration``:  фронтенд, предоставляющий форму регистрации новых пользователей
* ``frontend-accounting``: фронтед, предоставляющий форму авторизации и информацию по авторизованному пользователю
