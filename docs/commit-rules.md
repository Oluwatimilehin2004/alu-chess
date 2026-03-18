# Commit Rules

Ziel: kleine, nachvollziehbare, regelmäßig integrierte Änderungen.

## Grundregeln
- Committe häufig, aber nur bei stabilem Zwischenstand.
- Ein Commit = ein Thema.
- Jeder Commit muss kompilieren.
- Tests müssen mindestens grün bleiben.
- Keine toten Experimente in main/default branch.

## Commit-Frequenz (empfohlen)
- Nach jeder abgeschlossenen Mini-Aufgabe committen:
  - Struktur
  - Build/Config
  - Test-Block
  - Refactor-Block
  - Docs-Block
- Richtwert: alle 20-60 Minuten bei aktiver Arbeit oder nach jedem klaren Checkpoint.

## Conventional Commits
Format:

type(scope): summary

Beispiele:
- chore(setup): initialize sbt project structure
- feat(model): add immutable board representation
- test(controller): add observer notification tests
- refactor(view): simplify tui input handling
- docs(ai): update project memory and agent prompts

## Zulässige Typen
- feat
- fix
- refactor
- test
- docs
- chore

## Scope-Regeln
- Scope kurz und fachlich (setup, model, controller, view, test, docs, ci)
- Keine Misch-Scopes bei verschiedenen Themen

## PR-/Merge-Regeln
- Vor Push: compile + test lokal laufen lassen
- Keine großen Sammel-Commits ohne Grund
- PR-Beschreibung muss Zweck, Änderungen, Tests enthalten

## Mini-Checklist vor jedem Commit
- Build grün?
- Tests grün?
- Nur ein Thema enthalten?
- Commit message nach Schema?
- Keine sensiblen Daten enthalten?
