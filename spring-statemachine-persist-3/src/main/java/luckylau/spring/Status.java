package luckylau.spring;

/**
 * @Author luckylau
 * @Date 2022/3/26
 */
public enum Status {
    /**
     *
     */
    PENDING_APPROVAL("pending_approved", 1),
    PARTIAL_APPROVED("partial_approved", 2),
    APPROVED("approved", 3),
    REJECTED("rejected", 4),
    PENDING_ADMIN_APPROVE("pending_admin_approve", 5),
    PENDING_ADMIN_APPROVE_CONFIRM("pending_admin_approve_confirm", 6),
    PENDING_ADMIN_REJECT_CONFIRM("pending_admin_reject_confirm", 7),
    CHOICE("choice", 8);

    private String msg;

    private int statusCode;

    Status(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }

    public static Status valueOf(int statusCode) {
        Status[] values = values();
        for (Status status : values) {
            if (status.getStatusCode() == statusCode) {
                return status;
            }
        }
        throw new IllegalArgumentException("invalid status code");
    }

    public String getMsg() {
        return msg;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
