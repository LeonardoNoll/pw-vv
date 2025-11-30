# pw-vv — Instruções de execução

Projeto multimódulo com três serviços Quarkus:

- `manager/` — ponto de acesso principal (endpoints em `/manager`).
- `projects-trabalho-final/` — serviço de projetos.
- `users/` — serviço de usuários.

Resumo rápido
- O projeto foi desenvolvido para rodar com **Java 21 (LTS)**.
- Cada módulo é um app Quarkus e deve ser executado em modo de desenvolvimento com `./mvnw quarkus:dev`.
- Para rodar os três serviços ao mesmo tempo, execute `./mvnw quarkus:dev` em cada pasta (três terminais) e atribua portas distintas.

Pré-requisitos
- JDK 21 instalado e `JAVA_HOME` apontando para ele. Verifique com:

```bash
java -version
```

- Permissão de execução para o wrapper Maven (se necessário):

```bash
chmod +x ./mvnw
```


Ponto de acesso principal: `manager`

O `manager` expõe a maioria das operações do sistema em `/manager`. Exemplos:

- Gerar JWT (form-urlencoded):

```bash
curl -X POST -d "email=user@example.com&password=senha" http://localhost:8080/manager/jwt
```

- Registrar usuário (form-urlencoded):

```bash
curl -X POST -d "username=Nome&email=user@example.com&password=senha&confirmPassword=senha" \
  http://localhost:8080/manager/users/register
```

- Login (form-urlencoded):

```bash
curl -X POST -d "email=user@example.com&password=senha" \
  http://localhost:8080/manager/users/login
```

- Listar projetos (requer role `User`):

```bash
curl http://localhost:8080/manager/projects/list
```

- Criar projeto (JSON, requer role `User`):

```bash
curl -X POST -H "Content-Type: application/json" -d '{"name":"Meu Projeto","description":"Descrição"}' \
  http://localhost:8080/manager/projects/create
```

- Buscar projeto por ID:

```bash
curl http://localhost:8080/manager/projects/1
```

Notas sobre autenticação e permissões
- Alguns endpoints do `manager` exigem roles (`Admin` ou `User`). O JWT é usado para autenticação entre os serviços.
- Para chamadas protegidas via `curl`, passe o header `Authorization: Bearer <token>` retornado por `/manager/jwt`.


Problemas comuns
- Porta ocupada: aumente/alter a porta via `-Dquarkus.http.port` ou `application.properties`.
- Erro de versão Java: verifique `java -version` e `JAVA_HOME`.
- Permissão negada ao executar `./mvnw`: execute `chmod +x ./mvnw`.

