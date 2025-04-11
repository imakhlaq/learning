By default, Spring Boot configures this redirect URI as /login/oauth2/code/{registrationId}.
Therefore, for Google we’ll add the URI:
http://localhost:8080/login/oauth2/code/google