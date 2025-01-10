FROM openjdk:17-jdk-slim AS build

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar proyecto
COPY . /app

# Compilar el proyecto
RUN mvn clean install -DskipTests

# Usar una imagen ligera para el contenedor final
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR generado
COPY --from=build /app/target/Chat-Publico-0.0.1-SNAPSHOT.jar /app/chat-publico.jar

# Descargar y configurar wait-for-it.sh
RUN apt-get update && apt-get install -y curl && \
    curl -o /app/wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh && \
    chmod +x /app/wait-for-it.sh

# Exponer el puerto
EXPOSE 8085

# Comando para iniciar la aplicación
ENTRYPOINT ["./wait-for-it.sh", "mysql-chat-publico:3306", "--", "java", "-jar", "/app/chat-publico.jar"]