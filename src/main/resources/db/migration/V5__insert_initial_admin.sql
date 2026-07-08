INSERT INTO users (name, email, password, enabled, active, created_by, updated_by)
VALUES (
    'Initial Admin',
    'admin@example.com',
    '$2a$10$9okmZJnE2K5em9Eivnn0CuHMjS6lDDyrTtmC1Jn1NtMgBveX1X/1m',
    TRUE,
    TRUE,
    'flyway',
    'flyway'
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ADMIN'
WHERE u.email = 'admin@example.com';
