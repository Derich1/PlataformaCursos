# Etapa 1: build com Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# WORKDIR: define o diretório de trabalho dentro do container.
# /app: é o caminho absoluto dentro do container onde o código será colocado.
# Todas as instruções seguintes (como COPY, RUN, etc.) vão usar este diretório como base.
WORKDIR /app

# COPY: copia arquivos do host (seu computador) para o container.
# pom.xml: caminho do arquivo Maven no host (onde está o Dockerfile).
# .: destino dentro do container (ou seja, /app/pom.xml).
COPY pom.xml .

# Copia a pasta src (com o código fonte do projeto) para o diretório /app/src no container.
# src: pasta fonte do projeto local.
# ./src: destino dentro do container.
COPY src ./src

# Faz o build do projeto
RUN mvn clean install

# Etapa 2: imagem final, apenas com o JAR
FROM openjdk:21-jdk

# Novamente define /app como o diretório de trabalho dentro do novo container (da imagem final).
WORKDIR /app

# COPY: copia o JAR do estágio anterior.
# --from=builder: especifica que o arquivo está no estágio chamado builder (primeira imagem).
COPY --from=builder /app/target/Usuario-0.0.1-SNAPSHOT.jar /app/app.jar

# Comando para rodar a aplicação
CMD ["java", "-jar", "/app/app.jar"]
