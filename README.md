# Hollow Knight — 2D Action Platformer Engine & Tribute

[![Java](https://img.shields.io/badge/Language-Java_8%2B-ED8B00?logo=openjdk&logoColor=white)](#tech-stack)
[![Framework](https://img.shields.io/badge/Framework-libGDX-red)](#tech-stack)
[![Build](https://img.shields.io/badge/Build-Gradle-02303A?logo=gradle&logoColor=white)](#tech-stack)
[![Tools](https://img.shields.io/badge/Level_Design-Tiled_Map_Editor-green)](#tech-stack)

> A high-performance 2D action-platformer built from scratch in **Java** using the **libGDX** framework. Engineered with strict Object-Oriented Architecture (OOP), decoupled gameplay systems, state-driven combat, and custom tilemap parsing.

<p align="center">
  <img width="800" src="https://github.com/user-attachments/assets/e0ac7f8b-d09c-4270-bdd4-1bf7c52a008a" alt="Gameplay Showcase" />
</p>

##  Tech Stack & Engineering Core

* **Language & Core:** Java (JDK 8+)
* **Engine / Framework:** libGDX (Desktop backend)
* **Build Automation & Dependency Management:** Gradle
* **Architectural Patterns:** Object-Oriented Programming (OOP), Component-based State Management, Separation of Concerns (SoC)
* **Level Design & Asset Pipeline:** Tiled Map Editor (orthogonal / custom layers integration), TexturePacker / Atlas parsing

---

##  Implemented Core Systems

* **Player Controller & Movement Engine:** Custom physics handling fine-tuned platformer kinematics (variable jump height, horizontal dash physics, wall-slide/mantis claw interaction, and precise collision resolution).
* **Combat & Spell State Machine:**
  * Real-time attack hitboxes with knockback dynamics and directional striking.
  * Focus healing mechanism coupled with an interactive soul/resource meter.
  * Projectile and area-of-effect (AoE) spell integration (*Vengeful Spirit*, *Howling Wraiths*).
* **Extensible Charm System:** A modular modifier framework supporting 8 distinct charms that dynamically decorate and alter player attributes (attack speed, range, spell efficiency, etc.).
* **Enemy AI & Encounter Design:** Behavior loops implemented across 4 distinct base enemy classes with specialized behavior variants (patrol routes, agro ranges, aerial pathing), culminating in a structured multi-phase Boss Fight.
* **Animation & Rendering Pipeline:** Frame-accurate sprite sheet parsing, decoupled animation state controllers, layered environment rendering, and dynamic camera viewport tracking.
* **Level Architecture:** Multi-room environment loading designed from scratch using Tiled, handling dynamic object layers, spawn points, and boundary triggers.

---

<p align="center">
  <img src="https://github.com/user-attachments/assets/d298f4d4-50de-4412-85bd-6597f62ad0ae" width="48%" alt="Exploration Screenshot" />
  <img src="https://github.com/user-attachments/assets/d74d18b7-b11b-49c9-8a92-542060f31416" width="48%" alt="Combat Screenshot" />
</p>

---

##  Controls

| Action | Key Binding | Action | Key Binding |
| :--- | :--- | :--- | :--- |
| **Movement** | `Arrow Keys` (or Numpad) | **Basic Attack (Nail)** | `A` |
| **Jump** | `Space` | **Focus (Heal)** | `F` |
| **Dash** | `Shift` | **Vengeful Spirit** | `C` |
| **Mantis Claw** | Against Wall + `Arrow` | **Howling Wraiths** | `S` |

<p align="center">
  <img width="800" src="https://github.com/user-attachments/assets/bec55205-686e-4eee-937d-f4ad31cc787c" alt="Boss Fight Mechanics" />
</p>

---

##  Building and Running Locally

Ensure you have **Java Development Kit (JDK 8 or higher)** installed.

1. **Clone the repository:**
   ```bash
   
   ```

2. **Run via Gradle:**
   ```bash
   ./gradlew desktop:run
   ```
   
