# MiniDelivery — MVP GWT + UiBinder + RPC

## Requisitos
- JDK 11 (ou JDK compativel com GWT utilizado)
- Maven 3.9+

## Rodar (plugin legado, didatico)
```bash
mvn clean package
mvn gwt:run
```
Abrir `http://localhost:8888/index.html` (porta padrao do plugin; confirme no terminal).
Alternativa: executar o WAR `target/minidelivery.war` em container Java EE/Servlet **javax** (ex.: Tomcat 9), em contexto `/` ou ajustar URL conforme contexto.

**Nota:** projeto fornecido como exemplo de estudo; compilacao e execucao nao foram verificadas neste ambiente. A versao 2.10.0 do plugin Mojo e legada. Para projetos novos, considerar o plugin moderno tbroyer.

## Teste manual
1. Buscar vazio -> devem aparecer dois restaurantes de exemplo.
2. Cadastrar `Sushi Mix` / `Japonesa` -> aparece apos atualizar a busca.
3. Buscar `sushi` -> filtra apenas Sushi Mix.
4. Selecionar Sushi Mix, informar cliente `Ana`, produto `Temaki` e quantidade `2` -> pedido retornado com ID.
5. Tentar quantidade 0 -> barrado no cliente e no servidor.
6. Reiniciar o servidor -> cadastro volta aos restaurantes iniciais; tudo e em memoria.

## Fluxo tecnico
- `DeliveryEntryPoint` inicia a aplicacao e adiciona `DeliveryView` ao `RootPanel`.
- `DeliveryView.ui.xml` define os widgets, vinculados por `@UiField`.
- `@UiHandler` captura cliques.
- `DeliveryService` define contrato de RPC sincrono apenas como interface compartilhada.
- `DeliveryServiceAsync` espelha metodos com `AsyncCallback` no navegador.
- `GWT.create(DeliveryService.class)` cria o proxy do servico.
- `DeliveryServiceImpl` implementa as operacoes no servidor.
- O endpoint `/delivery/deliveryService` deve corresponder ao `@RemoteServiceRelativePath` + modulo `delivery`.
- `Restaurante` e `Pedido` sao DTOs serializaveis, com construtor publico sem argumentos.

## Limites do MVP
Sem banco, login, preco, estoque, catalogo, idempotencia ou controle de concorrencia entre multiplas instancias. Em producao, validar autorizacao, dados, transacoes e persistir em banco; evitar expor detalhes internos de erro para usuario.

## explicacao
"Modelei a interface com UiBinder, conectando os campos XML com @UiField e cliques com @UiHandler. No cliente, a interface assíncrona usa GWT RPC e AsyncCallback para nao bloquear a UI. A servlet implementa o contrato, valida os dados e manipula DTOs serializaveis. Para o MVP usei memoria, mas separaria repository e banco de dados numa evolucao."


## Evolucao MVP (Model-View-Presenter)
- `DeliveryView`: interface UiBinder, captura cliques e atualiza componentes; nao conhece RPC.
- `DeliveryPresenter`: valida a entrada, chama o RPC e trata `AsyncCallback`.
- `DeliveryEntryPoint`: cria e conecta View/Presenter/Service.
- `DeliveryServiceImpl` e os DTOs foram mantidos.

Depois de `mvn clean package`, execute o WAR com o Jetty independente (porta 8890) conforme validado no seu ambiente:

```powershell
mvn '-Djetty.http.port=8890' org.eclipse.jetty:jetty-maven-plugin:9.4.58.v20250814:run-war
```

Abra `http://127.0.0.1:8890/index.html`. A compilacao e a execucao deste pacote atualizado ainda precisam ser validadas localmente.
