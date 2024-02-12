package com.samruddhi.trading.equities.services.base;

import java.io.IOException;
import java.util.Optional;

public interface Authenticator {

    /** For authenticating users with redirect to a ULR */
    Optional<String> getAccessToken() throws IOException;

    String getApiKey();
}
