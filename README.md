# alu-chess

Eine vereinfachte Schach-Applikation in Scala – Lehrprojekt für den Kurs **Softwarearchitektur**.

Inspiration: [Lichess](https://lichess.org/), aber wesentlich einfacher.

---

## Tech Stack

| Komponente | Detail |
|---|---|
| Sprache | Scala 3.6.4 |
| Build Tool | sbt 1.10.7 |
| Testing | ScalaTest 3.2.19 |
| Coverage | sbt-scoverage 2.2.2 |
| Architektur | MVC + Observer |

---

## Setup

### Voraussetzungen

- JDK 17+ (empfohlen: JDK 21)
- sbt 1.10.x

### Kompilieren

```bash
sbt compile
```

### Tests ausführen

```bash
sbt test
```

### Anwendung starten

```bash
sbt run
```

### Coverage-Report erzeugen

```bash
sbt clean coverage test coverageReport
```

Der Report liegt anschließend unter `target/scala-3.6.4/scoverage-report/index.html`.

---

## Projektstruktur

```
src/main/scala/chess/
├── Chess.scala              # Entry Point (@main)
├── model/
│   ├── Piece.scala          # Figurtypen (enum)
│   ├── Board.scala          # Spielfeld (8×8, immutable)
│   └── Game.scala           # Spielzustand
├── controller/
│   ├── Controller.scala     # Spiellogik, Observable
│   └── ControllerInterface.scala
├── aview/
│   └── TUI.scala            # Text User Interface (Observer)
└── util/
    ├── Observable.scala      # Observer-Pattern
    └── Observer.scala

src/test/scala/chess/         # Spiegel der Hauptstruktur
docs/
├── ai-context.md             # Projekt-Memory für KI-Agenten
├── architecture-decisions.md # Architekturentscheidungen (ADR)
└── presentation-notes.md     # Wöchentliche Präsentationsnotizen
```

---

## Architektur

Das Projekt folgt dem **MVC-Pattern**:

- **Model** – Immutable Domain-Objekte (`Board`, `Piece`, `Game`). Kein `var`, kein `null`, keine Abhängigkeiten zu View oder Persistenz.
- **Controller** – Koordiniert Use Cases, implementiert `Observable`, hält den aktuellen Spielzustand.
- **View (aview)** – Text UI als `Observer`. Reagiert auf Änderungen und gibt das Board als Text aus.
- **Util** – Observer/Observable-Pattern als Infrastruktur für MVC.

Details: [`docs/architecture-decisions.md`](docs/architecture-decisions.md)

---

## Dokumentation

- [`docs/ai-context.md`](docs/ai-context.md) – Projekt-Memory für KI-gestützte Entwicklung
- [`docs/architecture-decisions.md`](docs/architecture-decisions.md) – Architekturentscheidungen im ADR-Format
- [`docs/presentation-notes.md`](docs/presentation-notes.md) – Präsentationsnotizen pro Woche
- [`docs/agent-prompts.md`](docs/agent-prompts.md) – Copy/Paste-Prompts für spezialisierte KI-Rollen
- [`docs/commit-rules.md`](docs/commit-rules.md) – Commit-Disziplin und Message-Format

---

## Teamregeln (Repo)

- [`.github/copilot-instructions.md`](.github/copilot-instructions.md) – persistente Copilot-Regeln für dieses Repository
- [`.github/pull_request_template.md`](.github/pull_request_template.md) – PR-Checkliste für konsistente Reviews

---

## Lizenz

Kursprojekt – keine öffentliche Lizenz.
