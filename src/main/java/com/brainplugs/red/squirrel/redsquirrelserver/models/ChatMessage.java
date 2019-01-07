package com.brainplugs.red.squirrel.redsquirrelserver.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class ChatMessage {
    private String userId;
    private String topic;
    private String message;
}
