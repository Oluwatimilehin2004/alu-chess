# AI Context – alu-chess

> Projekt-Memory-Dokument für KI-Agenten im IDE-Kontext.
> Bitte konsistent mit diesem Dokument arbeiten.

---

## 1. Projektziel

Entwicklung einer vereinfachten Schach-Applikation in Scala als Lehrprojekt im Kurs **Softwarearchitektur**.
Inspiration: Lichess, aber wesentlich einfacher.
Ziel: Inkrementelle Weiterentwicklung vom Monolith zu einer skalierbaren Architektur.

---

## 2. Aktuelle Phase und Scope

**Phase:** Woche 1–2 – Projektsetup, Grundstruktur und erste Zugmechanik

**Implementiert:**
- Monolith mit MVC-Architektur und Observer-Pattern
- Text UI (TUI) mit Zugeingabe (`"e2 e4"`)
- Funktionaler Stil im Domain-Layer (immutable, Option-basiert)
- Domain-Typen: `Piece`, `Color`, `Position`, `Move`, `Board`, `Game`, `GameStatus`
- `Board.move(Move)` gibt `Option[Board]` zurück
- `Game.applyMove(Move)` validiert Figurfarbe und wechselt Spieler
- `Controller.doMove` koordiniert Use Case und benachrichtigt Observer
- 63 Tests in 7 Suites, CI-Pipeline auf GitHub Actions
- Conventional Commits mit `.gitmessage`-Template

**Nicht im Scope (geplant für spätere Phasen):**
- Legale Züge pro Figurtyp (Turm, Läufer, Dame etc.)
- Spezialzüge (En Passant, Rochade, Bauernumwandlung)
- Schach/Matt/Patt-Erkennung
- Command-Pattern (Undo/Redo)
- HTTP-API / REST
- Persistenz (Datenbank, Dateisystem)
- Bot / KI-Gegner
- GUI (ScalaFX oder Web)
- Microservices
- Performance-Optimierung

---

## 3. Architekturregeln

- **MVC** als Grundstruktur
- Model darf **nicht** von View abhängen
- Model darf **nicht** von Persistenz abhängen
- Controller koordiniert Use Cases
- View kümmert sich nur um Ein-/Ausgabe
- Domain-Logik gehört in `model/`, nicht in `aview/` oder `controller/`
- Monolith modular halten
- Struktur so vorbereiten, dass spätere Extraktion von Adaptern möglich ist
- Observer-Pattern für MVC-Kommunikation

---

## 4. Scala-Coding-Regeln

- `val` statt `var` bevorzugen
- `null` vermeiden, `Option` verwenden
- `case class` für immutable Domain-Objekte
- Kein mutable State im Model-Layer
- Pure Functions bevorzugen
- Kleine Methoden bevorzugen
- Kein Overengineering
- Lesbarkeit vor Cleverness
- Scala-3-Features nutzen (enum, @main, Extension Methods wo sinnvoll)

---

## 5. Testregeln

- ScalaTest (WordSpec + Matchers)
- Jedes neue Domain-Feature bekommt Tests
- Happy Path **und** Edge Cases testen
- Tests sollen Verhalten beschreiben
- Sinnvolle Coverage priorisieren, nicht künstliche Coverage
- Ziel: 100% Coverage im Model-Layer

---

## 6. KI-Nutzungsregeln

- KI unterstützt, ersetzt aber kein architektonisches Denken
- Kleine Inkremente generieren, keine riesigen Systeme
- Generierten Code **immer** reviewen
- KI nach Begründungen fragen, nicht nur nach Implementierung
- Separate Agenten für Architektur, funktionalen Stil, Testing und Dokumentation
- Generierter Code muss in der Präsentation erklärbar bleiben

---

## 7. Tech Stack

| Komponente | Version / Tool |
|---|---|
| Sprache | Scala 3.6.4 |
| Build Tool | sbt 1.10.7 |
| Testing | ScalaTest 3.2.19 |
| Coverage | sbt-scoverage 2.2.2 |
| UI (aktuell) | TUI (Text) |
| Architektur | MVC + Observer |

---

## 8. Geplante Erweiterungen (Roadmap)

1. Vollständige Schachregeln
2. Command-Pattern (Undo/Redo)
3. GUI (ScalaFX)
4. HTTP-API (z.B. http4s oder Play)
5. Persistenz (Datei/DB)
6. Bot-API
7. Microservice-Extraktion
8. Performance / Concurrency
