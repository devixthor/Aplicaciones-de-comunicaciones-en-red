# Aplicaciones-de-comunicaciones-en-red
Prácticas de sexto semestre de comunicaciones en red - ESCOM IPN.

## Estructura del Proyecto

El proyecto está configurado como un proyecto multi-módulo de IntelliJ IDEA, donde cada práctica está aislada en su propio directorio/módulo independiente con su propio código fuente (`src/`) y sus librerías (`lib/`).

```text
Redes/
├── Practica1_Batalla_Naval/
│   ├── lib/                       # Librerías específicas (Jackson, etc.)
│   ├── src/                       # Código fuente y recursos de la práctica 1
│   │   ├── sonido/
│   │   ├── Cliente.java
│   │   ├── Servidor.java
│   │   └── ...
│   └── Practica1_Batalla_Naval.iml
├── Practica2_Reproductor_MP3/
│   ├── lib/                       # Librerías específicas para audio/MP3
│   ├── src/                       # Código fuente del reproductor MP3
│   │   └── Main.java
│   └── Practica2_Reproductor_MP3.iml
└── ...
```

## Cómo agregar una nueva práctica

Para agregar una nueva práctica (por ejemplo, `Practica3_...`):

1. **Desde IntelliJ IDEA**:
   - Ir a `File` -> `New` -> `Module...`
   - Indicar el nombre (ej. `Practica3_Nombre`) y verificar que la ubicación sea dentro de la carpeta raíz del repositorio.
   - IntelliJ generará el módulo con su carpeta `src/` y su archivo `.iml` automáticamente.

2. **O de forma manual**:
   - Crear el directorio `PracticaN_Nombre/` con subcarpetas `src/` y opcionalmente `lib/`.
   - Crear el archivo `PracticaN_Nombre.iml`.
   - Registrar el nuevo módulo en `.idea/modules.xml`:
     ```xml
     <module fileurl="file://$PROJECT_DIR$/PracticaN_Nombre/PracticaN_Nombre.iml" filepath="$PROJECT_DIR$/PracticaN_Nombre/PracticaN_Nombre.iml" />
     ```
