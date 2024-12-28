package com.myprojects.video_conferencing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RoomCreationRequest(
        @NotBlank
        @JsonProperty("room_name")
        String roomName
) {
}
