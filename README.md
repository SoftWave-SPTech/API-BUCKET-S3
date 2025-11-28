# API S3 - Microserviço de Gerenciamento de Arquivos

Microserviço especializado no gerenciamento de arquivos e integração com Amazon S3 do sistema SoftWave, desenvolvido com Spring Boot.

## Tecnologias Utilizadas

![Spring Boot](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Amazon S3](https://img.shields.io/badge/Amazon%20S3-FF9900?style=for-the-badge&logo=amazons3&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

### Dependências Principais

- **Spring Boot 3.5.6** - Framework principal
- **Spring Web** - APIs REST
- **AWS SDK S3 2.20.0** - Integração com Amazon S3
- **Lombok** - Redução de boilerplate
- **Spring Boot Test** - Testes

## Requisitos do Sistema

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)

- **Java** >= 21
- **Maven** >= 3.8.0
- **Conta AWS** com acesso ao S3
- **AWS CLI** (opcional, para configuração)

## Instalação e Configuração

### 1. Clone o Repositório

```bash
git clone <repository-url>
cd API-BUCKET-S3
```

### 2. Configuração AWS

#### Opção A: AWS CLI (Recomendado)

```bash
# Instalar AWS CLI
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# Configurar credenciais
aws configure
```

#### Opção B: Variáveis de Ambiente

```bash
export AWS_ACCESS_KEY_ID=sua-access-key
export AWS_SECRET_ACCESS_KEY=sua-secret-key
export AWS_REGION=us-east-1
```

### 3. Criar Bucket S3

```bash
# Via AWS CLI
aws s3 mb s3://softwave-arquivos-dev

# Via Console AWS
# 1. Acesse o console AWS S3
# 2. Clique em "Create bucket"
# 3. Nome: softwave-arquivos-dev
# 4. Região: us-east-1
# 5. Mantenha configurações padrão de segurança
```

### 4. Configuração de Ambiente

Crie um arquivo `application-local.yml` em `src/main/resources/`:

```yaml
spring:
  application:
    name: s3-service
  
  # Configuração Multipart
  servlet:
    multipart:
      enabled: true
      max-file-size: 10MB
      max-request-size: 10MB
      file-size-threshold: 2KB

# Servidor
server:
  port: 8091

# AWS Configuration
aws:
  region: ${AWS_REGION:us-east-1}
  access-key: ${AWS_ACCESS_KEY_ID:}
  secret-key: ${AWS_SECRET_ACCESS_KEY:}
  session-token: ${AWS_SESSION_TOKEN:}  # Opcional para roles temporárias
  
  s3:
    bucket: ${AWS_S3_BUCKET:softwave-arquivos-dev}
    url-expiration: ${S3_URL_EXPIRATION:3600}  # 1 hora em segundos

# CORS Configuration
cors:
  allowed-origins: ${CORS_ALLOWED_ORIGINS:http://localhost:5173,http://localhost:8080}
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: "*"
  allow-credentials: true

# File Management
file:
  allowed-extensions: pdf,doc,docx,txt,jpg,jpeg,png,gif,xlsx,xls
  max-size: 10485760  # 10MB em bytes
```

### 5. Variáveis de Ambiente

Configure as seguintes variáveis:

```bash
# AWS Credentials
export AWS_ACCESS_KEY_ID=sua-access-key-aqui
export AWS_SECRET_ACCESS_KEY=sua-secret-key-aqui
export AWS_REGION=us-east-1

# S3 Configuration
export AWS_S3_BUCKET=softwave-arquivos-dev
export S3_URL_EXPIRATION=3600

# CORS
export CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:8080
```

### 6. Instalação das Dependências

```bash
mvn clean install
```

### 7. Executar a Aplicação

#### Modo Desenvolvimento

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

#### Build e Execução

```bash
mvn clean package
java -jar target/BucketS3-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: http://localhost:8091

## Endpoints da API

### Upload de Arquivos

#### POST /api/s3/upload
Faz upload de um arquivo para o S3

**Request (multipart/form-data):**
- `file`: Arquivo a ser enviado
- `directory`: Diretório de destino (opcional)

**Response:**
```json
{
  "success": true,
  "message": "Arquivo enviado com sucesso",
  "data": {
    "fileId": "uuid-gerado",
    "originalName": "documento.pdf",
    "fileSize": 2048576,
    "contentType": "application/pdf",
    "s3Key": "documentos/documento.pdf",
    "publicUrl": "https://bucket.s3.amazonaws.com/documentos/documento.pdf"
  }
}
```

### Gerenciamento de Arquivos

#### GET /api/s3/files
Lista arquivos no bucket

#### GET /api/s3/files/{fileKey}
Obtém informações de um arquivo específico

#### DELETE /api/s3/files/{fileKey}
Remove um arquivo do S3

## Troubleshooting

### Problemas Comuns

1. **Erro de credenciais AWS**: Verifique se as chaves estão configuradas corretamente
2. **Bucket não encontrado**: Confirme se o bucket existe e o nome está correto
3. **Permissão negada**: Verifique as políticas IAM
4. **Upload falha**: Verifique o tamanho e extensão do arquivo

## Contribuição

1. Faça fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/s3-improvement`)
3. Commit suas mudanças (`git commit -m 'Melhora S3 service'`)
4. Push para a branch (`git push origin feature/s3-improvement`)
5. Abra um Pull Request

## Licença

Este projeto é propriedade da SoftWave SPTech e destina-se ao uso exclusivo do escritório Lauriano & Leão Sociedade de Advogados.

---

**Desenvolvido por:** SoftWave SPTech  
**Versão:** 0.0.1-SNAPSHOT  
**Data:** 2025
