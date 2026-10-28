# dynamic-bank-account-service

***Objetivo:***

- Gerenciar identidade (User, Client, Admin) e contas (Account)
- Validar credenciais para login
- Criar novos usuários (do tipo Client) com suas respectivas contas
- Fornecer a consulta da conta do usuário logado.

***Tecnologias:***

- JPA, com herança JOINED (`User`, `Client`, `Admin`)
- Spring Security: Resource Server (JWT HS256) e filtro de API key (`X-API-Key`)
- bcrypt para as senhas
- producer de Kafka (`audit.events`)