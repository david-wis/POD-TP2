# TPE 2 POD - Trabajo Práctico Especial 2 - Reclamos Urbanos 

## Integrantes:
- Liu, Jonathan Daniel 62533
- Vilamowski, Abril 62495
- Wischñevsky, David 62494

## Profesores:
- Turrin, Marcelo Emiliano
- Meola, Franco Román

## Objetivo:
Diseñar e implementar una aplicación de consola que utilice el modelo de programación
MapReduce junto con el framework Hazelcast para el procesamiento de reclamos urbanos,
basado en datos reales.

Para este trabajo se busca poder procesar datos de reclamos urbanos de las ciudades de
Nueva York, EEUU y Chicago, EEUU.

Los datos son extraídos de los respectivos portales de gobierno en formato CSV.

## Requisitos
- Java 17 o superior
- Maven
- Docker (si se quiere correr el compose que simula multiples nodos)

## Instalación
1. Ejecutar `mvn clean install` para compilar el proyecto y descargar las dependencias necesarias.
2. Dentro del directorio `server` ejecutar `run_server.sh` o `run_server_multi.sh` para iniciar el servidor.
3. Dentro del directorio `client` ejecutar `run_client.sh` para generar los scripts de cliente.
   Estos podrán encontrarse en el directorio `client/target/tpe2-g4-client-1.0-SNAPSHOT`.
4. Dentro del directorio `client/target/tpe2-g4-client-1.0-SNAPSHOT` ejecutar `query1.sh`, `query2.sh`, `query3.sh` o `query4.sh`.

### Comandos permitidos por cada cliente
- Query1
`sh query1.sh -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -Dcity=<NYC|CHI> -DinPath=XX -DoutPath=YY`
- Query2
`sh query2.sh -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -Dcity=<NYC|CHI> -DinPath=XX -DoutPath=YY -Dq=<GRADOS>`
- Query3
`sh query3.sh -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -Dcity=<NYC|CHI> -DinPath=XX -DoutPath=YY -Dw=<VENTANA>`
- Query4
`sh query4.sh -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -Dcity=<NYC|CHI> -DinPath=XX -DoutPath=YY -Dneighborhood=<BARRIO>`

Para más información sobre los comandos, consultar el siguiente [documento](/docs/TPE2%20Reclamos%20Urbanos.pdf)

### Parametros permitidos por el servidor
`sh run_server.sh -Dinterfaces=<IP> [-Dmode=<tcp|multicast> -Dmembers=<IP1:PUERTO1;IP2:PUERTO2;...>]`

## Docker compose
Para correr el proyecto en un entorno de múltiples nodos, se puede utilizar Docker Compose. 

```bash
./run_server_multi.sh
```
Por defecto el está configurado para levantar 5 contenedores con hazelcast en las ips `172.28.0.2`, `172.28.0.3`, `172.28.0.4`, `172.28.0.5` y `172.28.0.6`.
Para levantar menos o cambiar las ips, simplemente se debe modificar el docker-compose.yaml y volver a correr `mvn clean install`, `./run_server_multi.sh` y `./run_client.sh`.


