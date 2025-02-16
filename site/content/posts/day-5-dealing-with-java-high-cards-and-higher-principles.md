---
title: "Day #5: Dealing with Java: High Cards and Higher Principles 🃏"
date: 2025-02-12
description: "Building a complete multiplayer High Card game while exploring object-oriented features and clean code practices"
tags: ["day-5", "card-game", "object-oriented", "clean-code"]
showToc: true
TocOpen: false
---

Today we brought our card game to life, implementing a complete multiplayer High Card game while diving deeper into Java's object-oriented features.

> The code changes from this session can be found [here](https://github.com/caglarturali/javamastery/tree/7569dac8b009a2eeaac956fc7c6e193e248be812).

# Day 5 Summary

## Overview
Built a complete multiplayer High Card game while exploring Java's object-oriented features, modern syntax, and clean code practices.

## Environment & Tools
- Git workflow with feature branching
- zsh git shortcuts for efficient version control
- Proper package organization

## Concepts Covered

### Domain Modeling
- Record types for immutable data (Card, GameResult)
- Class design for mutable state (Player, Game)
- Enum implementation with behavior
- Clean separation of concerns

### Modern Java Features
- Records with validation
- Enhanced enum capabilities
- String formatting
- Stream operations for collections
- Optional for null safety

### Design Patterns & Principles
- Dependency Injection (IO handling)
- Immutable value objects
- Builder pattern for game setup
- Interface segregation
- Single Responsibility Principle

### Console Interface
- Interactive gameplay
- Input/Output abstraction
- Error handling
- User feedback
- Game state management

## Implementation Progress
- Created core game entities
- Implemented deck management
- Added player validation
- Built interactive console interface
- Established proper error handling

## Key Takeaways
- Java enums can contain behavior
- Records provide clean immutable data structures
- Dependency injection enhances testability
- Clean separation between domain and UI
- Proper error handling improves user experience

## Next Steps
Ready to explore:
- Score tracking system
- Deck reshuffling mechanism
- Different card game variants
- Unit testing implementation
- Player statistics

## Master's Reflection
The apprentice demonstrated excellent judgment in design choices, particularly in separating concerns and managing mutability. The evolution from a simple two-player game to a flexible multiplayer system showed growing comfort with Java's features. The intuitive grasp of when to use records vs classes and the thoughtful approach to dependency injection suggests a strong foundation in Java principles.