package com.parkinglot.api.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
    // DANGER !!!! For debug only
    public static final String DB_URL = "/h2-console/**";
    public static final String ORDERS = "/orders**";
    public static final String PARKINGLOTS = "/parkinglots**";
    public static final String PARKINGBOYS = "/parkingboys**";
    public static final String PLACEORDERFROMCUST = "/orders/**";
}
