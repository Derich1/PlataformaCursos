# Etapa 1: build com Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia os arquivos do projeto
COPY pom.xml .
COPY src ./src

# Faz o build do projeto
RUN mvn clean install

# Etapa 2: imagem final, apenas com o JAR
FROM openjdk:21-jdk

WORKDIR /app

# Copia o JAR da etapa anterior
COPY --from=builder /app/target/Curso-0.0.1-SNAPSHOT.jar /app/app.jar

# Comando para rodar a aplicação
CMD ["java", "-jar", "/app/app.jar"]
