package se.jaitco.queueticketapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = WebSocketEvent.WebsocketEventBuilder.class)
public class WebSocketEvent {

    Event event;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class WebsocketEventBuilder {
    }
}
