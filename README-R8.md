# MiniDelivery R8 — Testes do Presenter

A R8 e uma copia independente da R7, com extracao de `DeliveryViewContract`, para permitir testar o Presenter sem instanciar widgets GWT.

## Mudancas

- `DeliveryView` implementa `DeliveryViewContract`.
- `DeliveryPresenter` depende do contrato e nao de `DeliveryView` concreta.
- `DeliveryEntryPoint` conecta `view.setPresenter(presenter)`.
- JUnit 4.13.2 e sete testes com View e RPC falsos, sem dependencias Mockito.

## Executar

```powershell
mvn test
mvn clean package
mvn "-Djetty.http.port=8890" org.eclipse.jetty:jetty-maven-plugin:9.4.58.v20250814:run-war
```

**Importante:** `mvn test` verifica regras de apresentacao com dublês; testes de integracao do RPC exigem o Jetty. Os testes foram escritos, mas ainda nao foram executados neste ambiente.

## Pontos da entrevista

Separacao View/Presenter por contrato, injecao de dependencia via construtor, FakeService simula callbacks de sucesso/falha, FakeView permite verificar efeitos sem navegador.
