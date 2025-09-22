package io.github.monty.api.content.interfaces.rest.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ContentApiUrl {

    public static final String CONTENT_BASE_URL = "/content";
    public static final String CONTENT_VERSION = "/v1";
    public static final String CONTENT_V1_BASE_URL = CONTENT_BASE_URL + CONTENT_VERSION;

    @UtilityClass
    public static final class System {
        public static final String HEALTH_CHECK_URL = "/monitor/healthcheck";
    }

    @UtilityClass
    public static final class Album {
        public static final String ALBUM_LIST_URL = "/albums";
    }
}
