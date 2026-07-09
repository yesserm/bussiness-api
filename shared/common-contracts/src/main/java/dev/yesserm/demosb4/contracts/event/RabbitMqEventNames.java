package dev.yesserm.demosb4.contracts.event;

public final class RabbitMqEventNames {
    public static final String USER_EXCHANGE = "demosb4.user.events";

    public static final String USER_REGISTERED = "user.registered";
    public static final String USER_PROFILE_UPDATED = "user.profile.updated";
    public static final String ROLE_CHANGED = "user.role.changed";

    private RabbitMqEventNames() {
    }
}
