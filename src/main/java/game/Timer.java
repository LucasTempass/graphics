package game;

import java.time.LocalDateTime;

public class Timer {

	private LocalDateTime start;
	private LocalDateTime end;

	public void start() {
		start = LocalDateTime.now();
	}

	public void stop() {
		end = LocalDateTime.now();
	}

	public long getDuration() {
		return java.time.Duration.between(start, end).toMillis();
	}
}
