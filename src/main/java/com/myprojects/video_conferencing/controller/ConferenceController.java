package com.myprojects.video_conferencing.controller;

import com.google.protobuf.util.JsonFormat;
import com.myprojects.video_conferencing.dto.RoomCreationRequest;
import com.myprojects.video_conferencing.helper.MediaServerHelper;
import io.livekit.server.RoomServiceClient;
import jakarta.annotation.PostConstruct;
import livekit.LivekitModels;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConferenceController {
    @Value("${ms.host}")
    private String LivekitHost; // = "http://127.0.0.1:7880";
    @Value("${ms.secret}")
    private String LivekitSecret; // = "secret";
    @Value("${ms.key}")
    private String LivekitKey; // = "devkey";

    private final MediaServerHelper mediaServerHelper;

    private RoomServiceClient roomServiceClient;

    @PostConstruct
    public void init() {
        this.roomServiceClient = RoomServiceClient.createClient(
                LivekitHost,
                LivekitKey,
                LivekitSecret
        );
    }

    @GetMapping("/conference/media_server/jwt/create_room")
    public ResponseEntity<String> getMediaServerJwt_CreateRoom(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(mediaServerHelper.createToken_RoomCreation(name, "employee"));
    }

    @GetMapping("/conference/media_server/jwt/join_room")
    public ResponseEntity<String> getMediaServerJwt_JoinRoom(@RequestParam String name, @RequestParam String roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(mediaServerHelper.createToken_RoomJoin(name, "candidate", roomId));
    }

    @PostMapping("/conference/media_server/room/create")
    public ResponseEntity<String> createRoom(@RequestBody RoomCreationRequest roomCreationRequest) {
        Call<LivekitModels.Room> call = roomServiceClient.createRoom(roomCreationRequest.roomName());

        try {
            Response<LivekitModels.Room> response = call.execute(); // Use call.enqueue for async
            LivekitModels.Room room = response.body();

            System.out.println(JsonFormat.printer().print(room));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating room");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Ok");
    }

    @GetMapping("/conference/media_server/room")
    public ResponseEntity<List<String>> listRooms() {
        Call<List<LivekitModels.Room>> call = roomServiceClient.listRooms();

        try {
            Response<List<LivekitModels.Room>> response = call.execute();
            List<LivekitModels.Room> rooms = response.body();

            List<String> roomNames = rooms.stream().map(LivekitModels.Room::getName).toList();

            return ResponseEntity.status(HttpStatus.OK).body(roomNames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
