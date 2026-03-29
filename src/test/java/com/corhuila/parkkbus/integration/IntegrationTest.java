package com.corhuila.parkkbus.integration;

import org.junit.jupiter.api.Tag;

/**
 * Marker interface for integration tests.
 *
 * Integration tests in this package use Testcontainers to spin up
 * a real PostgreSQL instance and test the full application stack
 * through the HTTP layer.
 *
 * Run with: mvn test -Dgroups=integration
 */
@Tag("integration")
public @interface IntegrationTest {
}

