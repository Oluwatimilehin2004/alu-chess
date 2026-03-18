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
