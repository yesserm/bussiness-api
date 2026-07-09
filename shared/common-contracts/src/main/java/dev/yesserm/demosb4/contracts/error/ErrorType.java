package dev.yesserm.demosb4.contracts.error;

public final class ErrorType {

    public static final String VALIDATION = "https://demosb4.yesserm.dev/problems/validation";
    public static final String UNAUTHORIZED = "https://demosb4.yesserm.dev/problems/unauthorized";
    public static final String FORBIDDEN = "https://demosb4.yesserm.dev/problems/forbidden";
    public static final String NOT_FOUND = "https://demosb4.yesserm.dev/problems/not-found";
    public static final String CONFLICT = "https://demosb4.yesserm.dev/problems/conflict";
    public static final String INTERNAL_SERVER_ERROR = "https://demosb4.yesserm.dev/problems/internal-server-error";

    private ErrorType() {
    }
}
