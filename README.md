# Proyecto de Gestión de Entradas de Cine

Este proyecto está desarrollado con **JavaFX** y **JDBC** para la gestión de la venta de entradas y el control de salas de cine.

## Requisitos

Para ejecutar el proyecto, asegúrate de tener las siguientes dependencias instaladas:

- **JavaFX**: [Descargar JavaFX](https://openjfx.io/)
- **JDBC**: Incluye el controlador JDBC correspondiente para tu base de datos (MySQL, PostgreSQL, etc.).

## Configuración de la Base de Datos

**Nota:** El controlador de la base de datos para conectarse **NO** está incluido en este repositorio. Debes configurarlo localmente:

1. Descarga el controlador JDBC adecuado para tu base de datos.
2. Añádelo a tu proyecto.

```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private String url = "";
    private String user = "";
    private String password = "";

    public DatabaseConnection() {
        this.connection = connect();
    }

    public Connection connect() {
        System.out.println("Intentando conectar a la base de datos...");
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa!");
        } catch (ClassNotFoundException e) {
            System.out.println("Controlador JDBC no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return this.connection;
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("Conexión a la base de datos no está establecida.");
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
```

## Ejecución

1. Clona este repositorio:
   ```bash
   git clone https://github.com/tuusuario/TicketSuit.git
   ```

2. Navega a la carpeta del proyecto:
   ```bash
  cd TicketSuit
  ```

3. Compila y ejecuta el proyecto en tu IDE.
