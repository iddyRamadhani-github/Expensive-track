# Expense Tracker - Product Roadmap

This roadmap outlines the milestones, current achievements, and future planned enhancements for the **Expense Tracker App**.

---

## 🗺️ Project Vision

A highly polished, offline-first personal financial management application designed with a sleek, modern, Material 3 slate theme. It supports live local budgets, interactive canvas-drawn charts, and full dual-language support (English & Swahili) for localized use.

---

## 🚦 Phase Status Overview

| Phase | Milestone | Scope | Status |
| :--- | :--- | :--- | :--- |
| **Phase 1** | **Core Ledger Engine** | Room SQL database, repository layers, and basic CRUD capabilities. | **COMPLETED** |
| **Phase 2** | **Bespoke UI styling** | Sleek slate themes, high-contrast Category styles, beautiful custom dialogs. | **COMPLETED** |
| **Phase 3** | **Interactive Reports & Analytics** | Canvas-rendered 7-day Bar Chart, category percentages. | **COMPLETED** |
| **Phase 4** | **Bilingual Localization** | Seamless bilingual toggle (English and Swahili - Swahili localization of all categories, actions, dates, and alerts). | **COMPLETED** |
| **Phase 5** | **Polish & Verification** | Robolectric screenshot testing, adaptive layout checking. | **IN PROGRESS** |
| **Phase 6** | **Premium Expansion (Future)** | Recurring expenses, CSV exports, cloud sync options. | **PLANNED** |

---

## 🛠️ Detailed Roadmap Milestones

### 🟢 Completed Milestones

#### 1. Core Architecture & Local Storage (Phase 1)
- **MVVM Pattern**: Implemented standardized `ViewModel`, `Repository` and clean architectural flow.
- **Room Database Integration**: Set up modern Room persistence with transactional safety for reliable offline transaction storing.
- **Budget Limits**: Configurable local preferences (via `SharedPreferences`) tracking a customizable monthly budget cap in TZS.

#### 2. Slate Aesthetics & Interactive Visuals (Phase 2 & 3)
- **Modern Slate Palette**: Custom-crafted interface utilizing dynamic indigo `#6366F1` accents alongside deep slate surfaces (`0xFF161D31`).
- **Dynamic Chart System**: Built a performant daily expenditure canvas bar chart showing 7-day historic spending dynamically.
- **Category Decomposition**: Structured, high-fidelity breakdown showing percentage allocations across 9 different custom-colored expense classes.

#### 3. Complete Bilingual Support (Phase 4)
- **Fluid Toggle Engine**: Integrated an in-app language switch seamlessly swapping user layout structures instantly without interrupting state.
- **Swahili Language Support**: Translated visual categories (e.g. *Chakula*, *Usafiri*, *Vifaa*), currencies, dialog menus, errors, and custom date formatting for Swahili locales.

---

## 🚀 Future Milestones (Planned)

### 🟡 Phase 5: Verification & Adaptive Layouts (Current Focus)
- Check layout performance under heavy transactions.
- Enhance accessibility labels (`contentDescription`) across all canvas components.
- Prepare baseline snapshot validation for multiple screen densities.

### 🔴 Phase 6: Core Feature Extensions
- **Exporting Modules**: Ability to output expense logs via local CSV/XLS generation.
- **Smart Recurrencies**: Schedule repeating bills (e.g., monthly rent, weekly utilities) automatically.
- **Secure Cloud Backup**: Optional encrypted synchronization to a lightweight server or cloud backup engine.
