# API-BUCKET-S3

API para documentos jurídicos no bucket AWS S3 desenvolvida com Spring Boot e Maven.

## Funcionalidades

- ✅ Upload de documentos para AWS S3
- ✅ Listagem de documentos armazenados
- ✅ Exclusão de documentos
- ✅ Geração de URLs para download
- ✅ API RESTful com respostas JSON padronizadas
- ✅ Tratamento de erros centralizado

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Maven** para gerenciamento de dependências
- **AWS SDK v2** para integração com S3
- **JUnit 5** para testes

## Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- Conta AWS com bucket S3 configurado

## Configuração

### Variáveis de Ambiente

Configure as seguintes variáveis de ambiente:

```bash
export AWS_ACCESS_KEY_ID=your_access_key_id
export AWS_SECRET_ACCESS_KEY=your_secret_access_key
export AWS_REGION=us-east-1
export AWS_S3_BUCKET_NAME=your-bucket-name
```

### Arquivo de Configuração

Edite o arquivo `src/main/resources/application.properties` conforme necessário:

```properties
# Application Configuration
server.port=8080
spring.application.name=api-bucket-s3

# AWS S3 Configuration
aws.s3.bucket-name=${AWS_S3_BUCKET_NAME:legal-documents-bucket}
aws.s3.region=${AWS_REGION:us-east-1}
aws.access-key-id=${AWS_ACCESS_KEY_ID:}
aws.secret-access-key=${AWS_SECRET_ACCESS_KEY:}

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## Executando a Aplicação

### 1. Compilar o projeto

```bash
mvn clean compile
```

### 2. Executar os testes

```bash
mvn test
```

### 3. Executar a aplicação

```bash
mvn spring-boot:run
```

A aplicação será iniciada na porta 8080.

### 4. Build do JAR executável

```bash
mvn clean package
java -jar target/api-bucket-s3-1.0-SNAPSHOT.jar
```

## Endpoints da API

### Health Check

**GET** `/api/health`

Verifica se o serviço está funcionando.

```json
{
  "success": true,
  "message": "Serviço funcionando corretamente",
  "data": {
    "status": "UP",
    "timestamp": "2025-09-25T23:06:50.472103551",
    "service": "API Bucket S3",
    "version": "1.0-SNAPSHOT"
  }
}
```

### Welcome

**GET** `/api/`

Página de boas-vindas da API.

### Upload de Documento

**POST** `/api/documents/upload`

Faz upload de um documento para o S3.

**Parâmetros:**
- `file` (multipart/form-data): Arquivo a ser enviado
- `folder` (opcional): Pasta de destino no bucket

**Exemplo com curl:**
```bash
curl -X POST \
  http://localhost:8080/api/documents/upload \
  -F "file=@documento.pdf" \
  -F "folder=contratos"
```

### Listar Documentos

**GET** `/api/documents/list?prefix=folder`

Lista documentos armazenados no bucket.

**Parâmetros:**
- `prefix` (opcional): Prefixo para filtrar documentos

### Excluir Documento

**DELETE** `/api/documents/{key}`

Exclui um documento do bucket.

**Parâmetros:**
- `key`: Chave do documento no S3

### Gerar URL de Download

**GET** `/api/documents/{key}/download-url`

Gera uma URL para download do documento.

**Parâmetros:**
- `key`: Chave do documento no S3

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/softwave/s3api/
│   │   ├── S3ApiApplication.java          # Classe principal
│   │   ├── config/
│   │   │   └── S3Config.java              # Configuração AWS S3
│   │   ├── controller/
│   │   │   ├── HealthController.java      # Health check
│   │   │   └── DocumentController.java    # API de documentos
│   │   ├── service/
│   │   │   └── S3Service.java             # Lógica de negócio S3
│   │   └── dto/
│   │       ├── ApiResponse.java           # Resposta padronizada
│   │       └── DocumentInfo.java          # Informações do documento
│   └── resources/
│       └── application.properties         # Configurações
└── test/
    └── java/com/softwave/s3api/
        └── S3ApiApplicationTest.java       # Testes
```

## Formato das Respostas

Todas as respostas da API seguem o padrão:

```json
{
  "success": boolean,
  "message": "string",
  "data": object | null
}
```

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.
