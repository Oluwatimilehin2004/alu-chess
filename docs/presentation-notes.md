# Presentation Notes – alu-chess

> Wöchentliche Notizen für die Kurspräsentation.

---

## Woche 1–2: Projektsetup und Grundstruktur

### Was wurde gemacht?
- sbt-Projekt mit Scala 3.6.4 aufgesetzt
- MVC-Paketstruktur angelegt (`model`, `controller`, `aview`, `util`)
- Immutable Domain-Model: `Piece` (enum), `Board` (case class), `Game` (case class)
- Observer/Observable-Pattern für MVC-Kommunikation
- Text UI (TUI) mit einfacher Eingabeschleife
- ScalaTest-Suite mit WordSpec + Matchers
- sbt-scoverage für Coverage-Reports
- Projekt-Memory (`docs/ai-context.md`) für KI-Agenten definiert

### Architektonische Entscheidungen
- MVC mit Observer (ADR-001)
- Monolith-first (ADR-002)
- Funktionaler Stil im Domain-Layer (ADR-003)
- ScalaTest als Test-Framework (ADR-004)

### KI-Nutzung
- Vier spezialisierte Agenten definiert: Architecture, Functional Scala, Test & Coverage, Documentation
- Projekt-Memory-Dokument als persistenter Kontext für konsistente KI-Arbeit
- KI unterstützt beim Scaffolding, Coding-Stil und Testgenerierung
- Jeder generierte Code wird manuell gereviewed

### Präsentations-Fokus
- Warum MVC für ein Schachspiel?
- Wie sieht eine saubere Scala-3-Projektstruktur aus?
- Wie nutzt man KI methodisch (nicht nur als Autocomplete)?

---

## Woche 3–4: (Platzhalter)

_Geplant: Grundlegende Schachregeln, Zugvalidierung, mehr Tests_

---

## Woche 5–6: (Platzhalter)

_Geplant: Command-Pattern (Undo/Redo), GUI oder HTTP-API_

---

## Woche 7–8: (Platzhalter)

_Geplant: Persistenz, Bot-Integration, Architektur-Refactoring_

---

## Finale Präsentation: (Platzhalter)

_Zusammenfassung der Architektur-Evolution, Lessons Learned, KI-Reflexion_
