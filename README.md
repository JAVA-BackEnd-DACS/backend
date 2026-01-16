---
# ms-dacs2025-backend

Microservicio para backend con base de datos postgres

## Objetivo
![Texto alternativo](assets/infraestructura.png)


## Configuracion
![Texto alternativo](DACS - configuracion de infraestructura.pdf)


# Levantar en entorno local
```
mvn clean spring-boot:run
```

Opcionalmente se puede agregar el paŕametro:

-P local

```


### API de Prueba

Para chequear que la aplicacion levanto correctamente (Procedemos a consumir el servicio "/metrics/health"):

```
http://localhost:9003/metrics/health
```
