package dev.yesserm.demosb4.contracts.http;

public final class ServiceHeaders {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String CORRELATION_ID = "X-Correlation-Id";
    public static final String TRACE_ID = "X-Trace-Id";
    public static final String ADMIN_SETUP_KEY = "X-Admin-Setup-Key";

    private ServiceHeaders() {
    }
}
