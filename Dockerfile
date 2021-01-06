FROM java:8
EXPOSE 8082
ADD /target/eln-frontend-api.war eln-frontend-api.war
ENTRYPOINT ["java", "-jar", "/eln-frontend-api.war"]
