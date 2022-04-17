package luckylau.spring;

/**
 * @Author luckylau
 * @Date 2022/3/26
 */
public enum Event {
    /**
     * event
     */
    APPROVE(1),
    REJECT(2);

    private int code;

    Event(int code) {
        this.code = code;
    }

    public static Event valueOf(int code) {
        Event[] events = values();
        for (Event event : events) {
            if (event.getCode() == code) {
                return event;
            }
        }
        throw new IllegalArgumentException("invalid event code");
    }

    public int getCode() {
        return code;
    }
}
