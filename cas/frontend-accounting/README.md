Фронтед предоставляющий форму авторизации пользователя
===================================================

Angular2 использующее CAS SSO для авторизации пользователя по OpenID Connect протоколу
и форму содержащую информацию по авторизованному пользователю.

Сборка и настройка проекта
------------

1) Укажите путь к `backend` приложению в `./src/main/webapp/src/environments/environments.ts#serviceUrl` .
2) Укажите путь к серверу CAS SSO в `./src/main/webapp/src/environments/environments.ts#ssoUrl`.
2) Запустите приложение `ng serve`. Флагом `--port ****` можно указать отличный от `4200` порт.