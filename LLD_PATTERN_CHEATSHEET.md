# LLD Design Pattern Cheat Sheet

## The One Question That Separates the 3 Categories

Don't memorize "Creational / Structural / Behavioural" as three buckets. Think of it as **one question changing over the lifetime of an object**:

| Stage | Question | Category |
|---|---|---|
| Object doesn't exist yet | "How does this get built?" | **Creational** |
| Object exists, now needs to fit with others | "How do these already-built things connect/relate?" | **Structural** |
| Everything exists and is wired together | "What happens when they interact / how do they behave over time?" | **Behavioural** |

**Quick test for any scenario:**
- Remove the pattern → you'd be calling `new X()` wrong → **Creational**
- Remove the pattern → two existing things just don't fit/layer → **Structural**
- Remove the pattern → objects can't coordinate, react, or vary behavior at runtime → **Behavioural**

---

## Creational Patterns — "How is it born?"

| Pattern | Trigger Phrase | Discriminator vs. Closest Neighbor |
|---|---|---|
| **Singleton** | Only one instance, ever, guaranteed | vs Static class → Singleton supports interfaces, polymorphism, DI, lazy init; static class doesn't |
| **Factory Method** | ONE method, overridden by subclass, decides which ONE product to make | vs Simple Factory → does a **subclass** decide (Factory Method) or does a **parameter passed to one shared method** decide (Simple Factory, not GoF)? |
| **Abstract Factory** | A matching FAMILY of related products that must stay consistent | vs Builder → fixed/known-in-advance families (AF) vs. arbitrary client-decided combinations on ONE object (Builder) |
| **Builder** | ONE complex object, many optional parts, step-by-step / chained | vs Abstract Factory → one object incrementally assembled (Builder) vs. multiple different objects handed back as a consistent set (AF) |
| **Prototype** | Clone an existing, already-configured object instead of rebuilding | Real reason it's a named pattern: **polymorphic copying** — `clone()` works through a common interface without knowing the concrete type at compile time |

---

## Structural Patterns — "How do existing things fit together?"

| Pattern | Trigger Phrase | Discriminator vs. Closest Neighbor |
|---|---|---|
| **Adapter** | Interfaces don't match → translate one call into another | vs Facade → translating ONE incompatible interface (Adapter) vs. simplifying a complex multi-step sequence across many classes (Facade) |
| **Decorator** | Same interface, stack extra behavior on top, core still works without it | vs Proxy → adds optional behavior to an always-accessible object (Decorator) vs. controls/gates access to an object that may not even exist yet (Proxy) |
| **Facade** | Hide a fixed multi-step sequence behind ONE simple call | vs Abstract Factory → client gets a finished result, never touches internals (Facade) vs. client gets objects back to keep using (AF) |
| **Proxy** | Control, delay, or deny access to the real object | vs Decorator → is the wrapped thing optional (Decorator) or the only way the job gets done / access-gated (Proxy)? Flavors: Virtual (lazy load), Protection (access control) |
| **Composite** | Tree of parts — treat single item and group the same way, no `instanceof` | vs Decorator → branching tree of parent-child (Composite) vs. a straight line of wrapping (Decorator) |
| **Bridge** | Two hierarchies vary independently, connected by composition | vs Decorator → the held object is **load-bearing** (no fallback without it) not optional decoration. vs Abstract Factory → no "matching family" constraint, any abstraction + any implementation mix freely |
| **Flyweight** | Share the heavy repeated (intrinsic) data, keep unique (extrinsic) bits separate per instance | vs Singleton → Flyweight shares **many** instances (one per unique combo); Singleton shares **exactly one**, globally |

---

## Behavioural Patterns — "How do they act/communicate?"

| Pattern | Trigger Phrase | Discriminator vs. Closest Neighbor |
|---|---|---|
| **Iterator** | Walk through a collection without exposing how it's stored | Split into 2 interfaces (Collection + Iterator) so multiple simultaneous traversals don't corrupt each other's position |
| **Observer** | One object changes → many get notified automatically (one-way, passive) | vs Mediator → strictly one-way broadcast (Observer) vs. two-way, ongoing communication through a hub (Mediator) |
| **Strategy** | Swap which algorithm runs, at runtime, via composition (`setStrategy()`) | vs Factory Method → Strategy's job continues every call (behavior-focused); Factory Method's job ends once an object is handed back (creation-focused) |
| **State** | Object behaves differently based on internal state; **the state itself decides what's next** | vs Strategy → in State, the current state object triggers its own transition; in Strategy, the **client** picks and sets the strategy |
| **Template Method** | Fixed sequence of steps (`final` method), only specific steps vary, via inheritance | vs Strategy → shared skeleton to protect + inheritance (Template Method) vs. no skeleton, just one swappable behavior + composition (Strategy) |
| **Command** | Wrap ONE fully-parameterized action as an object — enables undo/queue/log | vs Strategy → Command's method is a discrete, already-loaded action (has `undo()`); Strategy's method is a reusable, repeatedly-callable algorithm |
| **Chain of Responsibility** | Pass along a line until exactly ONE handler deals with it (or none do) | vs Observer → only one (or a subset) acts, not everyone (CoR) vs. all observers get notified (Observer) |
| **Mediator** | Everyone communicates THROUGH one central hub, two-way | vs Observer → participants both send AND receive through the hub (Mediator) vs. strictly one-way (Observer). vs CoR → broadcasts to all/many at once (Mediator) vs. searches for exactly one handler (CoR) |
| **Memento** | Snapshot + restore state, without breaking encapsulation (private inner class trick) | vs Command → Memento restores a **whole saved state** wholesale; Command reverses a **specific action** via its own inverse logic |
| **Visitor** | Add new operations without touching existing classes — `accept(visitor)` → `visitor.visit(this)` (double dispatch) | Trade-off: great when operations change often but types are stable; bad fit when new types get added often (every visitor needs updating) |

---

## The "Why Creational/Structural/Behavioural" One-Liners

- **Abstract Factory (Creational) vs Facade (Structural):** AF hands the client objects to keep using; Facade hands the client a finished result and hides the objects entirely.
- **Builder (Creational) vs Decorator (Structural):** Builder's intermediate steps are NOT usable objects yet (unfinished until `.build()`); Decorator's every intermediate wrap IS already a fully usable object.
- **Adapter (Structural) vs Strategy (Behavioural):** Adapter fixes a mismatched interface once, structurally; Strategy is about choosing which behavior runs, repeatedly, at runtime.

---

## Recurring Personal Watch-Outs (from practice sessions)

1. **"Factory pattern" is ambiguous** — always specify Simple Factory vs. Factory Method. Simple Factory = one method, parameter decides. Factory Method = subclass decides.
2. **Aggregation methods (e.g., `getTotalSalary()`, `playTime()` in Composite):** use a **local variable** for the running total, not an instance field — otherwise repeated calls double-count.
3. **Bridge constructor direction:** the Implementor (e.g., `Device`) is passed INTO the Abstraction's (e.g., `Remote`'s) constructor — Abstraction holds a reference to Implementor, not the reverse.
4. **Singleton thread-safety:** double-checked locking needs the null check TWICE (once outside the lock, once inside) — a single check anywhere defeats the purpose.
