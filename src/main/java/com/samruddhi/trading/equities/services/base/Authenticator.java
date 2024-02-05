package com.samruddhi.trading.equities.services.base;

import java.io.IOException;
import java.util.Optional;

public interface Authenticator {

    Optional<String> getAccessToken() throws IOException;
}
