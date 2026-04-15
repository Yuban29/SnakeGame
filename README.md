# 🐍 SnakeGame para Android

Juego de la serpiente clásico desarrollado para Android con Java.

---

## PANTALLAZOS

![WhatsApp Image 2026-04-14 at 7 47 59 PM](https://github.com/user-attachments/assets/d0dbfdf9-60ae-4986-9ddc-7f6b3bc10608)


![WhatsApp Image 2026-04-14 at 7 48 02 PM](https://github.com/user-attachments/assets/426322e6-7f82-4aac-8fcf-1dc1bc16049c)





---
## 📁 Estructura del Proyecto

```
SnakeGame/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/snakegame/
│   │       │   ├── MainActivity.java       ← Actividad principal
│   │       │   └── SnakeGame.java          ← Lógica del juego
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml
│   │       │   └── values/
│   │       │       ├── colors.xml
│   │       │       ├── strings.xml
│   │       │       └── themes.xml
│   │       └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── gradle/wrapper/
│   └── gradle-wrapper.properties
├── build.gradle
├── settings.gradle
└── gradle.properties
```

---

## 🚀 Cómo abrir en Android Studio

1. Abre **Android Studio**
2. Selecciona **File → Open**
3. Navega hasta la carpeta `SnakeGame/` y haz clic en **OK**
4. Espera a que Gradle sincronice el proyecto
5. Conecta un dispositivo Android o inicia un emulador
6. Presiona el botón **▶ Run** (Shift+F10)

---

## 🎮 Controles

- **Desliza hacia arriba** → La serpiente sube
- **Desliza hacia abajo** → La serpiente baja
- **Desliza hacia la izquierda** → La serpiente va a la izquierda
- **Desliza hacia la derecha** → La serpiente va a la derecha
- **Toca la pantalla** en Game Over → Reiniciar juego

---

## 🏆 Características

- Movimiento fluido con SurfaceView en hilo separado
- Detección de colisiones (paredes y propio cuerpo)
- Comida generada aleatoriamente (evita aparecer sobre la serpiente)
- Puntuación en tiempo real (+10 por cada comida)
- Mejor puntuación guardada durante la sesión
- Velocidad progresiva (aumenta cada 50 puntos)
- Pantalla de Game Over con opción de reiniciar
- Cuadrícula visual para facilitar la jugabilidad

---

## ⚙️ Requisitos

- Android Studio Hedgehog (2023.1.1) o superior
- SDK mínimo: Android 5.0 (API 21)
- SDK objetivo: Android 14 (API 34)
- Java 8

---

## 🔧 Posibles mejoras

- [ ] Guardar puntuación máxima con SharedPreferences
- [ ] Agregar efectos de sonido
- [ ] Implementar niveles de dificultad
- [ ] Agregar pantalla de inicio
- [ ] Modo oscuro / diferentes temas de color
- [ ] Tabla de puntuaciones (leaderboard)
