FROM openjdk:21-jdk

# usar arquivo buildado, cria-lo em uma pasta app e deixa-lo com nome de app.jar
COPY target/Curso-0.0.1-SNAPSHOT.jar /app/app.jar

# comandos necessários para rodar o arquivo
CMD ["java", "-jar", "/app/app.jar"]