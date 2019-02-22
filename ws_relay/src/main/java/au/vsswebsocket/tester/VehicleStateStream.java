package au.vsswebsocket.tester;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

@Component
public class VehicleStateStream {

    private static final Logger logger = LoggerFactory.getLogger(VehicleStateStream.class);

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Value("${autonomic.vss.resourceUrl}")
    private String vssResourceUrl;

    @PostConstruct
    public void openStream() {
        try {
            // configure connection to the VSS resource
            WebSocket webSocket = new WebSocketFactory().setConnectionTimeout(5000).createSocket(vssResourceUrl);

            // add Authorization header with our auth token
            webSocket.addHeader("Authorization", "Bearer " + token());

            // add a listener to handle the messages we receive as a result of our query
            webSocket.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) {
                    logger.info("VSS Message: " + message);
                }
            });

            // create the connection
            webSocket.connect();
            //send our query
            webSocket.sendText(vssQuery());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create VehicleStateStream socket", e);
        }
    }

    private static String vssQuery() {
        return "{\"request_id\":\"samplespringboot:all_the_things_every_10s\",\"fields\":[\"_all_\"],\"per_asset_limit_interval_millis\":10000,\"scopes\":[]}";
    }

    private String token() {
        return oAuth2RestTemplate.getAccessToken().getValue();
    }

}