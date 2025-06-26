package swd.project.swdgr3project.service.proxy;

import java.util.Map;

/**
 * Service interface for Google OAuth operations.
 * This service acts as a proxy to the Google OAuth API.
 */
public interface GoogleAuthService {
    
    /**
     * Generate a Google OAuth authorization URL.
     *
     * @param redirectUri The redirect URI after authentication
     * @return The authorization URL
     */
    String getAuthorizationUrl(String redirectUri);
    
    /**
     * Exchange an authorization code for access token and user info.
     *
     * @param code The authorization code
     * @param redirectUri The redirect URI used in the authorization request
     * @return Map containing access token, refresh token, and expiration time
     * @throws Exception if token exchange fails
     */
    Map<String, String> exchangeCodeForToken(String code, String redirectUri) throws Exception;
    
    /**
     * Get user information from Google using an access token.
     *
     * @param accessToken The access token
     * @return Map containing user information (id, email, name, picture, etc.)
     * @throws Exception if user info retrieval fails
     */
    Map<String, String> getUserInfo(String accessToken) throws Exception;
    
    /**
     * Refresh an expired access token.
     *
     * @param refreshToken The refresh token
     * @return Map containing new access token and expiration time
     * @throws Exception if token refresh fails
     */
    Map<String, String> refreshAccessToken(String refreshToken) throws Exception;
    
    /**
     * Validate an ID token.
     *
     * @param idToken The ID token to validate
     * @return true if the token is valid
     */
    boolean validateIdToken(String idToken);
    
    /**
     * Revoke a token (logout).
     *
     * @param token The token to revoke
     * @return true if the token was revoked successfully
     */
    boolean revokeToken(String token);
}