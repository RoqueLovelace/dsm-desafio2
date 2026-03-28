# Agencia de Viajes DSM

App móvil hecha en Android donde se puede registrar, iniciar sesión y administrar destinos turísticos de forma sencilla. La idea es tener un catálogo donde se pueda ver, agregar, editar y eliminar lugares, todo en una sola aplicación.

## Descripción

Esta aplicación funciona como un pequeño sistema de gestión de destinos de viaje. El usuario primero crea su cuenta o inicia sesión, y luego entra a un catálogo donde puede ver solo los destinos registrados.

Desde ahí puede agregar nuevos destinos con información básica como nombre, país, precio, descripción e imagen. También puede entrar al detalle de cada destino, editarlo o eliminarlo si ya no lo necesita.

Las imágenes se guardan en formato Base64 directamente en la base de datos por porblemas con fire storage.

## Funcionalidades

* Registro de usuarios con correo y contraseña
* Inicio de sesión con Firebase Authentication
* Cierre de sesión
* Listado de destinos turísticos
* Creación de destinos con:

  * Nombre
  * País
  * Precio
  * Descripción
  * Imagen (convertida a Base64)
* Visualización de detalle de destino
* Edición de destinos
* Eliminación de destinos con confirmación
* Validación de campos en formularios

## Estructura del proyecto

```
com.ca220787.agenciaviajesdsm
├── LoginActivity
├── RegistroActivity
├── MainActivity
├── DestinoFormActivity
├── DetalleActivity
├── data
│   └── Destino
├── ui
│   └── DestinoAdapter
└── util
    └── ImgUtil
```

## Base de datos

```
destinos
  └── idDestino
        ├── nombDest
        ├── paisDest
        ├── precDest
        ├── descDest
        └── imagBase
```

## Autor

Chris Cortez
