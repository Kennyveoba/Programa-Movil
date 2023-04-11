<?php
// Conectarse a la base de datos
$conexion = mysqli_connect("localhost", "id20584189_1234", "D~AQ^+0MUg[Y06+6", "id20584189_programamovil");

// Verificar la conexión
if (mysqli_connect_errno()) {
    echo "Error al conectar con la base de datos: " . mysqli_connect_error();
    exit();
}

// Escapar los datos de entrada para evitar inyección SQL
$nombre = mysqli_real_escape_string($conexion, $_POST['nombre']);
$email = mysqli_real_escape_string($conexion, $_POST['email']);

// Crear la consulta SQL
$sql = "INSERT INTO estudiantes (nombre, email) VALUES ('$nombre', '$email')";

// Ejecutar la consulta
if (mysqli_query($conexion, $sql)) {
    echo "Registro insertado correctamente.";
} else {
    echo "Error al insertar registro: " . mysqli_error($conexion);
}

// Cerrar la conexión
mysqli_close($conexion);
?>
