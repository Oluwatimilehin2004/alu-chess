# Architecture Decisions – alu-chess

> Dokumentation architektonischer Entscheidungen im ADR-Stil (Architecture Decision Records).

---

## ADR-001: MVC als Grundarchitektur

**Kontext:** Das Projekt ist eine Schach-Applikation im Rahmen eines Softwarearchitektur-Kurses. Es muss verschiedene Architekturmuster demonstrieren können.

**Entscheidung:** MVC (Model-View-Controller) mit Observer-Pattern.

**Begründung:**
- Klare Trennung von Verantwortlichkeiten
- Gut geeignet für inkrementelle Erweiterung (TUI → GUI → Web)
- Standard-Pattern im universitären Kontext, gut präsentierbar
- Observer ermöglicht lose Kopplung zwischen Controller und Views

**Konsequenzen:**
- Model ist unabhängig testbar
- Mehrere Views können parallel existieren
- Controller wird zum zentralen Koordinationspunkt

---

## ADR-002: Monolith-First-Ansatz

**Kontext:** Das Projekt soll am Ende Microservices demonstrieren, aber inkrementell entwickelt werden.

**Entscheidung:** Start als modularer Monolith, spätere Extraktion.

**Begründung:**
- Einfacherer Start, weniger Infrastruktur nötig
- Modulare Paketstruktur erlaubt spätere Aufteilung
- "Monolith first" ist ein anerkanntes Pattern (Martin Fowler)

**Konsequenzen:**
- Keine verteilte Komplexität in der Anfangsphase
- Package-Grenzen müssen sauber gehalten werden
- Refactoring zu Services wird einfacher, wenn Grenzen klar sind

---

## ADR-003: Funktionaler Stil in Scala 3

**Kontext:** Scala erlaubt sowohl OOP als auch FP. Für den Kurs soll ein klarer Stil gelten.

**Entscheidung:** Funktionaler Stil im Domain-Layer, pragmatischer Mix im Rest.

**Begründung:**
- Immutable Domain-Objekte sind einfacher zu testen und zu begründen
- `case class`, `enum`, `Option` statt `null` reduzieren Fehlerquellen
- Reine Funktionen machen Logik vorhersagbar
- Controller/View dürfen pragmatisch `var` für Zustand verwenden (z.B. Observer-Liste)

**Konsequenzen:**
- Model-Layer hat keinen mutable State
- Board-Operationen erzeugen neue Board-Instanzen
- Etwas höherer Speicherverbrauch (akzeptabel für Schach)

---

## ADR-004: ScalaTest als Test-Framework

**Kontext:** Es gibt mehrere Test-Frameworks für Scala (ScalaTest, MUnit, Specs2).

**Entscheidung:** ScalaTest mit WordSpec-Stil und Matchers.

**Begründung:**
- Ausgereift, gut dokumentiert, weit verbreitet
- WordSpec erlaubt lesbare, BDD-ähnliche Tests
- Matchers geben klare Fehlermeldungen
- Gute IDE-Integration

**Konsequenzen:**
- Tests lesen sich als Spezifikation
- sbt-scoverage für Coverage-Reports

---

## ADR-005: Package-Struktur ohne Uni-Prefix

**Kontext:** Typischerweise werden Packages nach der Organisation benannt (z.B. `de.htwg.se.chess`).

**Entscheidung:** Einfaches `chess`-Package ohne Uni-Prefix.

**Begründung:**
- Kürzer, weniger verschachtelt
- Portabler, keine institutionelle Bindung
- Ausreichend für ein Kursprojekt

**Konsequenzen:**
- Kürzere Imports
- Weniger Verzeichnistiefe

---

## ADR-006: Position und Move als eigene Typen

**Kontext:** Züge könnten als Tupel `(Int, Int, Int, Int)` oder als Strings dargestellt werden. Im Domain-Model braucht es eine klare Repräsentation.

**Entscheidung:** `Position` und `Move` als eigene `case class`-Typen mit Parsing aus algebraischer Notation.

**Begründung:**
- Type Safety: Der Compiler verhindert, dass Zeile und Spalte verwechselt werden
- Algebraische Notation (`"e2"`, `"e2 e4"`) ist die Sprache des Schachs – direkte Übersetzung in Domain-Typen
- Parsing und Validierung zentral in Companion Objects (`Position.fromString`, `Move.fromString`)
- Trennung von Darstellung (String) und Semantik (Position/Move) folgt dem DDD-Prinzip

**Konsequenzen:**
- Board-API wird ausdrucksstärker: `board.move(Move(...))` statt `board.move(r1, c1, r2, c2)`
- Neue Regeln (legale Züge, Schach) können direkt auf `Position`/`Move` arbeiten
- Geringer Overhead (zwei kleine case classes), kein Overengineering

---

## ADR-007: Option-basierte Zugvalidierung

**Kontext:** Wenn ein ungültiger Zug versucht wird (leeres Feld, fremde Figur, Position außerhalb), muss das System reagieren.

**Entscheidung:** `Board.move` und `Game.applyMove` geben `Option[Board]` bzw. `Option[Game]` zurück. Kein Exception-Wurf.

**Begründung:**
- Ungültige Züge sind **erwartbares Verhalten**, keine Ausnahmesituation
- `Option` macht im Typsystem sichtbar, dass ein Zug fehlschlagen kann
- Aufrufer (Controller) muss explizit mit `None` umgehen – kein versehentliches Ignorieren
- Passt zum funktionalen Stil: pure Function ohne Seiteneffekte
- Pattern Matching auf `Some`/`None` ist idiomatisch und lesbar

**Konsequenzen:**
- Controller prüft das Ergebnis und gibt `Boolean` an die View zurück
- Keine try/catch-Blöcke im Zug-Flow
- Spätere Erweiterung (z.B. `Either[MoveError, Game]` für detaillierte Fehlermeldungen) ist einfach möglich
