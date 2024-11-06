# Pagamento de Mensalidades
Aplicação web que ajuda a realizar **pagamentos via pix** ou **verificar transferências anteriores**. Projeto foi iniciado com intuito de auxiliar com os registros e transferencias de um centro religioso local.


# Arquitetura
Detalhes da Implementação:
 - A implementação é feita com **Spring Boot** e **JDK 17**.
 - As tranferências Pix são gerenciadas através da Api do **Banco EFI**.
 - Os dados são persistidos por gerencia do **JPA**, e em nosso caso, numa conexão a um banco **PostgreSQL**.
 - O **FrontEnd** é gerado *server side* com uso do **Thymeleaf** apenas com **js** e **css**.

<!--## Entidades Relacionais
   ```mermaid
   classDiagram
   class Usuário {
	   Integer id;
	   String login;
	   String name;
	   String password;
   }
   class Pagamento {
	   Integer id;
	   Integer paymentMonth;
	   Integer paymentYear;
	   String pixUrl;
	   LocalDateTime expiresAt;
	   LocalDateTime closedAt;
   }
   Usuário -- Pagamento : 1 - n
   ```-->

## Classes
   ```mermaid
   classDiagram
   class Usuário {
	   <<model>>
	   - Integer id;
	   String login;
	   String name;
	   String password;
   }
   class Cobrança {
	   <<model>>
	   - Integer id;
	   Integer paymentMonth;
	   Integer paymentYear;
	   String pixUrl;
	   User user;
	   LocalDateTime expiresAt;
	   LocalDateTime closedAt;
   }
   Usuário <--> Cobrança : 1 - n


	class EfiHelper {
		<<interface - component>>
		+ exibirListaDeCobrancas () void
		+ criarCobrancaImediata (devedor, valor, expiraEm) Response
		+ criarQrCode (CobrancaImediata.Request) Response

	}
	
	EfiHelper --> CobrancaImediata_Response
	EfiHelper --> CobrancaImediata_Request
	EfiHelper --> GerarQRCode_Response
	
	class CobrancaImediata_Request {
	    <<record>>
		CalendarioSemCriacao calendario,
		Devedor devedor,
		Valor valor,
		String chave,
		String solicitacaoPagador
	}
	CobrancaImediata_Request --> CalendarioSemCriacao
	CobrancaImediata_Request --> Valor
	CobrancaImediata_Request --> Devedor
	
	class CobrancaImediata_Response {
		<<record>>
		Calendario calendario,
		String txid,
		String revisao,
		String status,
		Valor valor,
		String chave,
		Devedor devedor,
		String solicitacaoPagador,
		Loc loc,
		String location,
		String pixCopiaECola
	}
	CobrancaImediata_Response --> Calendario
	CobrancaImediata_Response --> Valor
	CobrancaImediata_Response --> Devedor
	CobrancaImediata_Response --> Loc
	
	class GerarQRCode_Response {
		<<record>>
		String qrcode,
		String imagemQrcode,
		String linkVisualizacao
	}
	class Calendario {
		<<record>>
		String criacao;
		int expiracao;
	}
	class CalendarioSemCriacao {
		<<record>>
	    int expiracao ;
	}	
	class Devedor {
		<<record>>
		String cpf;
		String nome.
	}
	class Valor {
		<<record>>
		String original
		Valor (double value)
	}
	class Loc {
		<<record>>
		int id;
		String location;
		String tipoCob;
		String criacao;
	}

	UserService -- Usuário
	class UserService {
		<<service>>
		+ tryToLogin(login, password) User
   	}

	PaymentService -- Cobrança
	class PaymentService {
	    <<service>>
		+ createOrGetPayment(user, month, year) Payment
		+ createPaymentTable(user) Map[]
	}
   ```



# Acesso e Execução

 - Um deploy da aplicação pode ser **acessado** através do [*link*](https://pagamento-carnes-production.up.railway.app/).
	> por estar em um plano gratuito da **railway**, é normal receber *502 Bad Gateway* em um primeiro acesso. Basta recarregar a página por uma segunda vez.

   - Para executar localmente é recomendado a utilização do **Docker**. Com as descrições do ***DockerFile*** atual, o projeto será executado no modo de ***desenvolvimento***. Mockando as conexões da **api pix** e do **banco de dados**
## Para uma execução com dados mockados
	SPRING_PROFILES_ACTIVE=dev


	 
## Para uma execução em produção
	SPRING_PROFILES_ACTIVE=prd

	EFI_BASE64_P12=#certificado_p12_em_base64
	EFI_CLIENT_ID=#credencial_de_projeto
	EFI_CLIENT_SECRET=#credencial_de_projeto
	EFI_URL=https://pix.api.efipay.com.br #default

	PGHOST=#endereço_do_seu_banco_Postgree
	PGPORT=#porta_ouvida_pelo_banco_de_dados
	PGDATABASE=#nome_do_banco_de_dados_utilizado

# Testes e Validações
Para realizar testes localmente execute o comando abaixo na pasta raiz da aplicação.
	
	gradle test

<!--# Atualização e Monitoramento-->
