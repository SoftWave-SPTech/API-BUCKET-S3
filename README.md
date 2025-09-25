# API-BUCKET-S3

Uma API REST em Java para gerenciamento de documentos jurídicos no Amazon S3, desenvolvida com Spring Boot.

## Características

- ✅ Upload de arquivos para S3
- ✅ Download de arquivos do S3  
- ✅ Listagem de arquivos com filtros
- ✅ Exclusão de arquivos
- ✅ Verificação de existência de arquivos
- ✅ Geração de URLs públicas
- ✅ Organização por pastas
- ✅ Tratamento de erros personalizado
- ✅ Validação de arquivos
- ✅ Logs detalhados

## Tecnologias

- **Java 17**
- **Spring Boot 3.2.1**
- **AWS SDK 2.21.29**
- **Maven**

## Configuração

### Variáveis de Ambiente

Configure as seguintes variáveis de ambiente:

```bash
AWS_ACCESS_KEY_ID=your-access-key-id
AWS_SECRET_ACCESS_KEY=your-secret-access-key
AWS_REGION=us-east-1
AWS_S3_BUCKET_NAME=your-bucket-name
```

### Arquivo application.yml

```yaml
aws:
  access-key-id: ${AWS_ACCESS_KEY_ID:your-access-key-id}
  secret-access-key: ${AWS_SECRET_ACCESS_KEY:your-secret-access-key}
  region: ${AWS_REGION:us-east-1}
  s3:
    bucket-name: ${AWS_S3_BUCKET_NAME:your-bucket-name}
```

## Instalação e Execução

### Pré-requisitos

- Java 17+
- Maven 3.6+
- Conta AWS com bucket S3 configurado

### Executando o projeto

```bash
# Clonar o repositório
git clone https://github.com/SoftWave-SPTech/API-BUCKET-S3.git
cd API-BUCKET-S3

# Compilar o projeto
mvn clean compile

# Executar os testes
mvn test

# Executar a aplicação
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

## Endpoints da API

### Health Check
```
GET /api/s3/health
```

### Upload de Arquivo
```
POST /api/s3/upload
Content-Type: multipart/form-data

Parâmetros:
- file: arquivo a ser enviado
- folder: pasta de destino (opcional)
```

**Exemplo de resposta:**
```json
{
    "key": "documentos/contrato_20241225_143022.pdf",
    "fileName": "contrato.pdf",
    "message": "Arquivo enviado com sucesso",
    "url": "https://your-bucket.s3.us-east-1.amazonaws.com/documentos/contrato_20241225_143022.pdf"
}
```

### Download de Arquivo
```
GET /api/s3/download/{key}
```

### Listar Arquivos
```
GET /api/s3/list?prefix=documentos/
```

**Exemplo de resposta:**
```json
[
    {
        "key": "documentos/contrato_20241225_143022.pdf",
        "fileName": "contrato_20241225_143022.pdf",
        "size": 1024576,
        "lastModified": "2024-12-25T14:30:22.000Z",
        "etag": "\"d41d8cd98f00b204e9800998ecf8427e\""
    }
]
```

### Deletar Arquivo
```
DELETE /api/s3/delete/{key}
```

### Gerar URL Pública
```
GET /api/s3/presigned-url/{key}?expiration=60
```

### Verificar Existência de Arquivo
```
GET /api/s3/exists/{key}
```

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/softwave/s3api/
│   │   ├── config/           # Configurações AWS
│   │   ├── controller/       # Controllers REST
│   │   ├── dto/             # DTOs para requests/responses
│   │   ├── exception/       # Tratamento de exceções
│   │   ├── service/         # Lógica de negócio
│   │   └── S3ApiApplication.java
│   └── resources/
│       └── application.yml
└── test/
    └── java/com/softwave/s3api/
        └── S3ApiApplicationTest.java
```

## Segurança

- Configure as credenciais AWS usando variáveis de ambiente
- Nunca commite credenciais no código fonte
- Use IAM roles com permissões mínimas necessárias
- Configure CORS adequadamente para produção

## Permissões AWS Necessárias

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:GetObject",
                "s3:PutObject",
                "s3:DeleteObject",
                "s3:ListBucket"
            ],
            "Resource": [
                "arn:aws:s3:::your-bucket-name",
                "arn:aws:s3:::your-bucket-name/*"
            ]
        }
    ]
}
```

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
