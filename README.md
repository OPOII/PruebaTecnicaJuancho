# PruebaTecnicaJuancho

## Instrucciones

1. Clona o descarga el proyecto
2. Abre el proyecto en tu editor favorito, preferiblemente en Intellij Idea
3. Compila el proyecto

## Recomendaciones antes de compilar el proyecto

La base de datos es PostgresSQL, por lo que, se debe de tener una base de datos local en postgress con la siguiente información.

1. Nombre de la base de datos `juanchopresta`
2. usuario postgres
3. contraseña la de su eleccion

igualmente, estos datos puede cambiarlos en el application properties del proyecto, para personalizarlo a su gusto

## Como usasr el proyecto

Para poder ver el API, se debe de acceder a la siguiente url despues de springboot ejecute sin problemas el proyecto.
url=http://localhost:8080/swagger-ui/index.html.
Despues, habran dos url que seran publicas, la de registrar y autenticar. Se registra el usuario con los parametros que son especificados ahí, en el trmcurrency, debera ingresar
manualmente la tasa de cambio de su moneda preferida, es decir, si escoje COP, entonces la conversion a la fecha de hoy seria 1 USD-> 4250 COP. Luego de registrarse, la 
url de autenticarse debera de ingresar su usuario y contraseña, donde la respuesta sera el token que debera de usar para la utenticación en las demas URL.

Para ello, copias el jwt sin las comillas (""), busca el boton que dice "Authorize" junto a un candado al lado, lo abres y pegas el jwt. Al hacer esto, tendras el acceso a los
demas endpoints para probarlos.

## A tener en cuenta.
Para agregar monedas, es de suma importancia que busque las monedas a partir del simbolo de la criptomoneda, de lo contrario, el programa fallara y arrojara un error.
Esto no detendra ni destruira la ejecución del programa, por lo que podra corregir el error ingresando el simbolo correcto de la cripto moneda.
Si tiene dudas acerca de los simbolos de las criptomonedas, puede consultarlo [aqui](https://github.com/yonilevy/crypto-currency-symbols)

