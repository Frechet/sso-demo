Провайдер авторизации через социальную сеть Вконтакте для Keycloak
===================================================

### Настройка

Скопийруйте 2 файла по образцу и подобию настройки для twitter 
(realm-identity-provider-vkontakte.html и realm-identity-provider-vkontakte-ext.html),
папка ${keycloakHome}/themes/base/admin/resources/partials.

### Деплой проекта

Пусть Keycloak запущен локально, как `standalone` на порту `8080`,
тогда для сборки проекта и деплоя в Keycloak достаточно команды:

    mvn clean install wildfly:deploy

Теперь, если Вы войдёте в Keycloak, то во вкладке `Indentity Providers` Вы обнаружите данный провайдер для подключения.
Добавте провайдер и сохраните. Заметим, что на стороне Вконтакте должно быть создано App (VK Developers - https://vk.com/dev/manuals),
в котором будет разрешён редирект из `edpoint` провайдера, а само App будет зарегистрированно в провайдере, как клиент.