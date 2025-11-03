package io.github.monty.api.user.interfaces.rest.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserApiUrl {

    public static final String USER_BASE_URL = "/user";
    public static final String USER_VERSION = "/v1";
    public static final String USER_V1_BASE_URL = USER_BASE_URL + USER_VERSION;

    @UtilityClass
    public static final class System {
        public static final String HEALTH_CHECK_URL = "/monitor/healthcheck";
    }

    @UtilityClass
    public static final class Login {
        public static final String USER_LOGIN_URL = "/users/login";
    }
}
