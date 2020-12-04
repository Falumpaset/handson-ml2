# Importer
The Importer is deployed on a virtual machine at `https://ui.os.da-rz.net/` and has
the following IP Address `185.80.184.223`

The Importer is configured as a Systemd Service.

## Systemd

### Accepting multiple Formats

The importer accepts multiple file formats. To support this, it creates 2 folders, named default and relion.
For usual openimmo imports the "default" folder should be used and for .csv tables and images provided by relion the "relion"
folder should be used.

### For general configuration
`/etc/systemd/system/importer.service`

### Executable start file
`/var/immomio/packages/importer-job/`

### Importer Configuration File
In the above folder of `/var/immomio/packages/importer-job/` we need to place
a file called `importer.conf`

```bash
APP_NAME=importer
LOG_FOLDER=/var/immomio/sys/log/importer-job
PID_FOLDER=/var/immomio/run
JAVA_HOME=/usr/lib/jvm/java-8-oracle/jre/bin/
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/usr/lib/jvm/java-8-oracle/jre/bin/
JAVA_OPTS='-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=production'
```

###

## Nginx
For Elasticsearch and the Importer we have configured Nginx to do SSL termination. The configuration
file is as follows:

```bash
 cat /etc/nginx/sites-enabled/default 
server {
  listen 9200;

  ssl on;
  ssl_certificate /etc/nginx/certs/host.cert;
  ssl_certificate_key /etc/nginx/certs/host.key;
  ssl_session_timeout 5m;
  ssl_protocols TLSv1.2 TLSv1.1 TLSv1;
  ssl_ciphers HIGH:!aNULL:!eNULL:!LOW:!MD5;
  ssl_prefer_server_ciphers on;

  auth_basic "Elasticsearch";
  auth_basic_user_file /etc/nginx/es-password;

  location / {
    proxy_pass http://172.16.100.4:9200;
    proxy_http_version 1.1;
    proxy_set_header Connection "Keep-Alive";
    proxy_set_header Proxy-Connection "Keep-Alive";
  }
}

server {
  listen 443;

  ssl on;
  ssl_certificate /etc/nginx/certs/host.cert;
  ssl_certificate_key /etc/nginx/certs/host.key;
  ssl_session_timeout 5m;
  ssl_protocols TLSv1.2 TLSv1.1 TLSv1;
  ssl_ciphers HIGH:!aNULL:!eNULL:!LOW:!MD5;
  ssl_prefer_server_ciphers on;

  location / {
    proxy_pass http://localhost:8080;
    proxy_http_version 1.1;
    proxy_set_header Connection "Keep-Alive";
    proxy_set_header Proxy-Connection "Keep-Alive";
  }
}

```