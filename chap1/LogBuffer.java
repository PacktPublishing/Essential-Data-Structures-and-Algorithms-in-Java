import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LogBuffer {

    static class LogEntry {
        LocalDateTime timestamp;
        String level;
        String message;
        String source;

        LogEntry(String level, String message, String source) {
            this.timestamp = LocalDateTime.now();
            this.level = level;
            this.message = message;
            this.source = source;
        }

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return String.format("[%s] %s [%s] %s", timestamp.format(formatter), level, source, message);
        }
    }

    private LogEntry[] buffer;
    private int head; // Next position to write
    private int size; // Number of entries in the buffer
    private final int capacity; // Maximum number of entries the buffer can hold

    public LogBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new LogEntry[capacity];
        this.head = 0;
        this.size = 0;
    }

    // Add log entry (circular buffer, order of insertion matters but not sorted)
    public void addLog(String level, String message, String source) {
        LogEntry entry = new LogEntry(level, message, source);

        if (size < capacity) {
            buffer[head] = entry;
            head = (head + 1) % capacity;
            size++;
        } else {
            // If the buffer is full, remove the oldest entry
            removeOldestEntry();
            buffer[head] = entry;
            head = (head + 1) % capacity;
            size++;
        }
    }

    private void removeOldestEntry() {
        if (size > 0) {
            LogEntry oldest = buffer[(head - size) % capacity];
            for (int i = 0; i < size; i++) {
                if (buffer[i] == oldest) {
                    buffer[i] = null;
                    break;
                }
            }
            head = (head + size - 1) % capacity;
        }
    }

    // Get recent logs (most recent first)
    public List<LogEntry> getRecentLogs(int count) {
        if (count > size) {
            return new ArrayList<>();
        } else {
            List<LogEntry> recent = new ArrayList<>();
            for (int i = head; i < head + count; i++) {
                LogEntry entry = buffer[i];
                if (entry != null) {
                    recent.add(entry);
                }
            }
            return recent;
        }
    }

    public static void main(String[] args) {
        // Example usage:
        LogBuffer logBuffer = new LogBuffer(10);

        logBuffer.addLog("INFO", "User logged in", "Web App");
        logBuffer.addLog("WARNING", "Invalid password", "Web App");
        logBuffer.addLog("ERROR", "Database connection failed", "Web App");

        List<LogEntry> recentLogs = logBuffer.getRecentLogs(5);
        for (LogEntry entry : recentLogs) {
            System.out.println(entry.toString());
        }
    }
}

