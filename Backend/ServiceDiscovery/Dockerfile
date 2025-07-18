# Usa a imagem base do OpenJDK 21
FROM eclipse-temurin:21-jdk

# Argumento para copiar o .jar
ARG JAR_FILE=target/*.jar

# Copia o .jar para dentro do container
COPY ${JAR_FILE} app.jar

# Expõe a porta padrão do Eureka (8761)
EXPOSE 8761

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "/app.jar"]
