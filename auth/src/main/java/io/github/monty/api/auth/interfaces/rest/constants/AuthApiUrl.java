package io.github.monty.api.auth.interfaces.rest.constants;

public class AuthApiUrl {

    //  Health Check 기본 URL
    public static final String AUTH_HEALTH_CHECK_BASE_URL = "/auth/monitor/healthcheck";

    //  Base URL with versioning
    public static final String AUTH_V1_BASE_URL = "/auth/v1";

    //  토큰 생성
    public static final String AUTH_CREATE_TOKEN = "/token";

    public static final String AUTH_REFRESH_TOKEN = "/token";
}
