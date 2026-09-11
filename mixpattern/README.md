
---

**Q1 — `car-customization`**
*Build a `Car` object that supports optional features: sunroof, leather seats, GPS, spoiler. Some cars will have 2 features set, others up to all 4. Implement this such that the client can set only what they need, and the final `Car` object is always fully and correctly constructed. Explain why you chose this approach over a large constructor with many parameters.*

**Q2 — `weather-sensor-singleton`**
*Implement a `WeatherSensor` class that simulates an expensive calibration step on creation. Ensure that no matter how many parts of your program request the sensor, they always get the exact same instance — and make it safe for multiple threads to request it simultaneously. Explain what would break if you didn't handle the multi-threaded case.*

**Q3 — `furniture-style-sets`**
*Implement a furniture ordering system with two styles — Victorian and Modern — each producing a matching `Sofa`, `Chair`, and `Table`. The client picks a style once, and every piece produced afterward must belong to that same style — no mixing allowed. Explain how your design structurally prevents mixing styles by mistake.*

**Q4 — `xml-to-json-bridge-data`**
*You're given an existing `XMLDataSource` class (you can stub it) that only returns data as XML strings. Your new `ReportingEngine` only knows how to work with a `JSONData` interface. Make `ReportingEngine` work with `XMLDataSource` without modifying either class. Explain why modifying `XMLDataSource` directly would have been the wrong approach.*

**Q5 — `checkout-shipping-estimate`**
*Implement `TaxService`, `ShippingRateService`, and `PromoService` as separate classes, each with their own methods. Then implement a single `getShippingEstimate()` call that internally coordinates all three in the correct order and returns one final number. Explain why hiding this sequence behind one call is useful, and what a caller who needs fine-grained control could still do.*

**Q6 — `message-formatting-effects`**
*Implement a `Message` object where a client can apply any combination of Bold, Italic, and Strikethrough formatting — including the same effect applied twice — without the underlying "send message" logic ever changing. Explain why this scales better than a `Message` subclass for every formatting combination.*

**Q7 — `video-subscription-gate`**
*Implement a `VideoStream` class that starts buffering when `play()` is called. Add a subscription check so that only subscribed users can actually trigger playback — but `VideoStream` itself should have zero knowledge that subscriptions exist. Explain where the subscription-check logic lives and why it isn't inside `VideoStream`.*

**Q8 — `org-chart-salary`**
*Implement `Employee` and `Department`, where a `Department` can contain both individual `Employee`s and other nested `Department`s. Implement `getTotalSalary()` so it works correctly at any level of nesting with zero type-checking (`instanceof`) anywhere in your code. Explain what specific typing decision makes the recursion work.*

**Q9 — `shape-render-engine-mix`**
*Implement `Circle` and `Square` shapes, each of which can be rendered using either `OpenGLRenderer` or `DirectXRenderer` — any shape with any renderer, without creating a class per combination (`OpenGLCircle`, `DirectXCircle`, etc.). Explain which side holds a reference to which, and why that direction matters.*

**Q10 — `forest-tree-rendering`**
*Simulate rendering 100,000 trees of type Oak or Pine. Each tree type has heavy shared data (texture, mesh) that should exist only once per type, while each individual tree has its own unique x/y position. Implement this so the heavy data is never duplicated per tree. Explain what specifically would go wrong if you accidentally stored position inside the shared type data.*

**Q11 — `spreadsheet-template-clone`**
*Implement a `SpreadsheetTemplate` class with some nested/complex data (e.g., a list of `CellStyle` objects). Implement cloning so a new spreadsheet can be created from an existing fully-configured template without re-building it from scratch. First implement it with a shallow copy and demonstrate the bug it causes (mutate a cell style on the "copy" and show it also changes the original), then fix it with a proper deep copy.*

---