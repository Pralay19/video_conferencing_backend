package com.myprojects.video_conferencing.helper;

import io.livekit.server.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class MediaServerHelper {

    @Value("${ms.secret}")
    private String LivekitSecret; // = "secret";
    @Value("${ms.key}")
    private String LivekitKey; // = "devkey";

    public String createToken_RoomCreation(String name, String identity) {

        AccessToken token = new AccessToken(LivekitKey,LivekitSecret);

        token.setName(name);
        token.setIdentity(identity);
//        token.setMetadata("metadata");
        token.addGrants(
                new RoomCreate(true)
        );

        return token.toJwt();
    }

    public String createToken_RoomJoin(String name, String identity, String roomId) {
        AccessToken token = new AccessToken(LivekitKey,LivekitSecret);

        token.setName(name);
        token.setIdentity(String.valueOf(new Random().nextInt(10000)));
//        token.setMetadata("metadata");
        token.addGrants(
                new RoomCreate(true),
                new RoomJoin(true),
                new RoomName(roomId),
                new CanPublish(true),
                new CanPublishData(true),
                new CanSubscribe(true)
        );

        return token.toJwt();
    }
}
