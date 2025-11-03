package io.github.monty.api.auth.interfaces.rest.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AuthApiUrl {

    public static final String AUTH_BASE_URL = "/auth";
    public static final String AUTH_VERSION = "/v1";
    public static final String AUTH_V1_BASE_URL = AUTH_BASE_URL + AUTH_VERSION;

    @UtilityClass
    public static final class System {
        public static final String HEALTH_CHECK_URL = "/monitor/healthcheck";
    }

    @UtilityClass
    public static final class Token {
        public static final String CREATE_TOKEN = "/token";
        public static final String REFRESH_TOKEN = "/token";
    }
}
