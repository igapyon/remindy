# Developer Notes

This document describes the project structure, build, and run steps for remindy.

## Directory Layout

```
remindy/
├── src/
│   ├── main/
│   │   ├── java/jp/igapyon/remindy/
│   │   │   ├── Main.java                  # entry point
│   │   │   ├── RemindyConstants.java      # app constants
│   │   │   ├── command/                   # feature commands
│   │   │   ├── conv/                      # Outlook CSV converter
│   │   │   ├── core/                      # minute ticker framework
│   │   │   ├── loader/                    # JSON loaders
│   │   │   ├── logic/                     # message builder
│   │   │   ├── ui/                        # popup UI
│   │   │   ├── util/                      # tray icon helper
│   │   │   └── vo/                        # value objects
│   └── main/resources/
│       ├── images/
│       ├── input/
│       ├── reminders.json
│       └── proverbs.json
├── target/                                # build outputs
├── pom.xml
└── README.md
```

## Build and Run

### Run from Maven

```sh
mvn exec:java
```

### Build a runnable JAR

```sh
mvn package
```

Output: `target/remindy-*-shaded.jar`

Run example:

```sh
java -jar target/remindy-1.20250805.1-shaded.jar
```

## Key Files

| File or Class | Role |
| --- | --- |
| `Main.java` | Entry point. Initializes tray icon and `MinuteTicker`. |
| `MinuteTicker.java` | Runs `MinuteCommand` instances every minute. |
| `StartupCommand.java` | Shows version and upcoming reminders at startup. |
| `NotifyCommand.java` | Sends tray notifications on schedule. |
| `PopupCommand.java` | Shows popup when a reminder time matches. |
| `PikoMouseCommand.java` | Slight mouse move for sleep prevention (optional). |
| `MessageBuilder.java` | Builds reminder and proverb messages. |
| `ReminderLoader.java` / `ProverbLoader.java` | Loads `reminders.json` / `proverbs.json`. |
| `OutlookCsvToRemindersConv.java` | Converts Outlook CSV to reminders JSON. |

## Versioning Rules

- Maven (`pom.xml`): `1.<yyyyMMdd>.<n>` (example: `1.20251212.1`).
- App (`RemindyConstants.VERSION`): `<yyyyMMdd><alpha>` (example: `20251212a`).
- Keep the date and release order aligned between the two.

## Notes

- `StartupCommand` runs once at the beginning of `MinuteTicker.start()`.
- If `SystemTray` is not supported, tray notifications are skipped safely.
- `reminders.json` and `proverbs.json` are read from the classpath.
- `RemindyConstants.REMINDER_EXTERNAL_PATH` can override the reminders path.
- `MinuteTicker` triggers about 10 seconds before the exact minute.
