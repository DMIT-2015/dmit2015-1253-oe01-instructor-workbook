package dmit2015.view;

import dmit2015.restclient.KeycloakLoginMpRestClient;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Named
@SessionScoped
public class LoginSession implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private KeycloakLoginMpRestClient loginRestClient;
    @Inject
    @ConfigProperty(name = "keycloak.oidc.clientId")
    private String oidcClientId;

    @Inject
    @ConfigProperty(name = "keycloak.oidc.clientSecret")
    private String oidcClientSecret;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private JsonObject loginResponsePayload;

    @Getter @Setter
    private LocalDateTime expiresIn;

    public String getAuthorization() {
        if (loginResponsePayload == null || !loginResponsePayload.containsKey("access_token")) {
            return null;
        }
        return "Bearer " + loginResponsePayload.getString("access_token");
    }

    public String checkForToken() {
        if (loginResponsePayload == null || expiresIn == null) {
            return "/login?faces-redirect=true";
        }

        if (LocalDateTime.now().isAfter(expiresIn)) {
            try {
                String refreshToken = loginResponsePayload.getString("refresh_token");
                loginResponsePayload = loginRestClient.refreshToken(
                        refreshToken, oidcClientId, oidcClientSecret, "refresh_token");
                expiresIn = LocalDateTime.now()
                        .plusSeconds(loginResponsePayload.getInt("expires_in"));
            } catch (Exception e) {
                loginResponsePayload = null;
                username = null;
                expiresIn = null;
                return "/login?faces-redirect=true";
            }
        }

        return null;
    }
}