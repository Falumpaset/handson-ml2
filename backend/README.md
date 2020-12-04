# Analyze Code

## Findbugs

Site `http://findbugs.sourceforge.net/`

Run `mvn findbugs:check`

## PMD

Site `https://pmd.github.io/`

Run `mvn pmd:check -Dpmd.verbose=true`

## Checkstyle

Site `http://checkstyle.sourceforge.net/`

Run `mvn checkstyle:check -Dcheckstyle.failOnViolation=false`

# Generate keys for keycloak SSO service

```
openssl genrsa -out server.pem 1024
openssl rsa -in server.pem -pubout > server.pub
openssl pkcs8 -topk8 -nocrypt -in server.pem -out server-pkcs8.pem
```
 