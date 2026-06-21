# Expense Tracker - Engineering Developer Log (DevLog)

This log contains the incremental updates, technical revisions, architecture details, and core design choices made during the development of the **Expense Tracker App**.

---

## 📈 DevLog History

### Entry 4: Complete In-App English & Swahili Localization
**Date:** June 11, 2026
**Author:** AI Agent
- **Background**: The app required full support for English and Swahili to cater to users in Tanzania.
- **Implemented Changes**:
  - Introduced the stateful `app_language` tracker inside `ExpenseViewModel.kt` initialized from local storage preferences.
  - Developed a centralized localization map `Loc` providing fast localized utility bindings `Loc.t(key, language)`. Includes translations of all predefined spending categories (*Chakula*, *Usafiri*, *Afya*), field configurations (*Amount*, *Description*), input hints, budget configurations, errors, and actions.
  - Implemented an elegant in-app language switch action inside the top `HeaderSection`.
  - Refactored `formatDateString(millis, locale)` to support custom languages properly, ensuring date strings format as Swahili month abbreviations when selected.
  - Fixed a Kotlin compilation issue by replacing the deprecated `String.toUpperCase()` function with modern `String.uppercase()`.

### Entry 3: Modern UI Overhaul (Visual Slate Theme)
**Date:** June 10, 2026
**Author:** AI Agent
- **Background**: Rebuilding the old interface into a beautiful deep dark slate experience.
- **Implemented Changes**:
  - Changed primary visual color accents to an artistic Violet/Indigo `#6366F1` and background slate `#0F172A` / `#161D31`.
  - Added visual depth to cards through custom-selected subtle borders (`1.dp, Color(0x80334155)`).
  - Enhanced dialog visuals with cleaner borders, soft margins, and uniform rounded corners (`16.dp`).

### Entry 2: Custom Canvas 7-Day Expenditure Graph
**Date:** June 08, 2026
**Author:** AI Agent
- **Background**: Needed a fast, modern visualization component without adding heavy web dependencies.
- **Implemented Changes**:
  - Wrote a performant bar chart using `androidx.compose.foundation.Canvas`.
  - Calibrated height ranges proportionally based on maximum daily spent values.
  - Mapped individual days backward gracefully from the current weekday utilizing proper Calendar metrics.

### Entry 1: Core Architecture & SQLite Engine
**Date:** June 05, 2026
**Author:** AI Agent
- **Background**: Initiated the project with robust enterprise-grade foundations.
- **Implemented Changes**:
  - Defined the core schema using `Room` entities.
  - Created transactional database instance classes with thread-safe singleton checks.
  - Tied repository flows straight to view models via standard Kotlin `StateFlow` structures for zero-lag UI reaction.

---

## 🛠️ Architectural Synopsis

### State Flow Diagram
```
[Database (Room)] <-- [ExpenseRepository] <-- [ExpenseViewModel]
                                                    |
                                            (Exposes StateFlows)
                                                    |
                                                    v
                                         [ExpenseTrackerApp (UI)]
                                      (Reacts to data & language changes)
```

- **ViewModel**: Standardizes operations. Persists the `app_language` via `SharedPreferences` to ensure app starts in the user's last picked language.
- **Form Validation**: Strict local entry error check preventing invalid decimals or blank values from hitting database.
- **Performance**: Heavy filtering operations and chart coordinates are memoized with Jetpack Compose `remember` hashes linked directly to the data keys.
