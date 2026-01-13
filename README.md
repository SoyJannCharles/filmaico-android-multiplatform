# 🎬 Filmaico – Android Multiplatform Streaming Platform

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Android%20TV-000000?style=for-the-badge&logo=androidtv&logoColor=white" />
  <img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" />
  <img src="https://img.shields.io/badge/ExoPlayer-4285F4?style=for-the-badge&logo=google&logoColor=white" />
</p>

---

## 📄 Resumen del Proyecto

**Filmaico** es una plataforma de streaming desarrollada para **Android Mobile y Android TV**, diseñada con foco en **performance**, **escalabilidad** y **mantenibilidad**.

El proyecto fue desarrollado **de forma individual**, aplicando buenas prácticas de arquitectura moderna en Android, con una base **multimodular**, **Clean Architecture** y una UI construida completamente con **Jetpack Compose**, incluyendo optimizaciones específicas para dispositivos Android TV de bajos recursos.

Este repositorio funciona tanto como **base de producto real** como **proyecto de portfolio técnico**.

---

## 🎯 Objetivo del Proyecto

> **¿Cómo diseñar y construir una aplicación de streaming moderna que sea escalable, eficiente y mantenible, soportando múltiples factores de forma (Mobile y TV) dentro del ecosistema Android?**

Filmaico busca responder a esta pregunta mediante:
- una arquitectura clara y modular
- separación estricta de responsabilidades
- componentes reutilizables
- optimización de recursos para TV

---

## 🧠 Alcance y Decisiones Técnicas

- Soporte para **Android Mobile y Android TV**
- Arquitectura desacoplada orientada a features
- Sistema de foco **custom** para navegación en TV
- Seguridad y configuración dinámica usando Firebase
- Preparado para crecimiento futuro (nuevos features, backend propio, nuevas plataformas)

---

## 🏗 Arquitectura

El proyecto sigue los principios de **Clean Architecture**, dividido en capas claras:

### Capas principales
- **Presentation** → UI, ViewModels, estados
- **Domain** → lógica de negocio, modelos, casos de uso
- **Data** → repositorios, DTOs, fuentes de datos

### Organización multimodular
- `app`, `app-mobile`, `app-tv` → puntos de entrada
- `feature:*` → funcionalidades independientes (series, anime, detalle, etc.)
- `domain:*` → lógica de negocio por feature
- `data:*` → implementación de acceso a datos
- `core:*` → UI compartida, navegación, red, configuración

Cada módulo tiene responsabilidades claras y puede evolucionar de forma independiente.

---

## 📺 Optimización para Android TV

Filmaico incluye un enfoque específico para TV:

- Sistema de **foco personalizado**
- Navegación fluida con D-Pad
- Componentes Compose optimizados (Cards de bajo consumo)
- Manejo eficiente de memoria y recomposición

---

## 🛠️ Tecnologías y Herramientas

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose
- **Arquitectura:** Clean Architecture + MVVM
- **DI:** Hilt
- **Persistencia:** Room, DataStore
- **Streaming:** ExoPlayer
- **Networking:** Retrofit + HTTP/3 client
- **Backend as a Service:** Firebase
    - Authentication
    - Firestore
    - Storage
    - Remote Config
- **Build System:** Gradle multimodular + plugins personalizados

---

## 📁 Estructura del Repositorio

```text
Filmaico/
|
├── app-mobile/         # Código específico para Android/iOS
├── app-tv/             # Código específico para Android TV/Apple TV
│
├── feature/            # Módulos de funcionalidades (UI + ViewModels)
│   ├── serie/          # Listado y lógica de series
│   ├── seriedetail/    # Detalle de series
│   ├── anime/          # Sección de anime
│   └── ...
│
├── domain/             # Lógica de negocio pura (Use Cases & Models)
│   ├── serie/
│   ├── stream/
│   └── ...
│
├── data/               # Implementación de datos (Repos, API, DB)
│   ├── serie/
│   ├── user/
│   └── ...
│
├── core/               # Módulos transversales compartidos
│   ├── ui/             # Design System y componentes comunes
│   ├── navigation/     # Lógica de navegación centralizada
│   ├── network/        # Configuración de clientes HTTP/Retrofit
│   └── config/         # Flags y variables de entorno
│
└── build-logic/        # Convenciones de Gradle y scripts de compilación

```

---

## 🔐 Seguridad y Configuración

- Uso de **Firebase Remote Config** para valores dinámicos
- Separación de configuraciones sensibles
- Preparado para migración futura a backend propio

> Nota: este repositorio no expone credenciales reales.

---

## 🚀 Estado del Proyecto

Filmaico se encuentra en **desarrollo activo** y está pensado para:
- distribución futura
- evolución de features
- integración con backend dedicado

---

## 👤 Autor

Desarrollado por **Jann Charles**  
Proyecto individual, guiado y ejecutado como parte de un proceso de crecimiento profesional.