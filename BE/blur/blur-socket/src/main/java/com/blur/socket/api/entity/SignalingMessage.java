package com.blur.socket.api.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignalingMessage {
	public enum MessageType {
	    OFFER,
	    ANSWER,
	    ICE_CANDIDATE;
	}
	
    private MessageType type;  // signaling message의 유형 (offer, answer, candidate)
    private String roomId;  // 연결된 방 ID
    private String message;  // signaling message의 내용 (offer, answer, candidate SDP)
    // getters and setters
}
