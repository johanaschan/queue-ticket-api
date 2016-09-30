package se.jaitco.queueticketapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = WebsocketEvent.WebsocketEventBuilder.class)
public class WebsocketEvent {

    Event event;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class WebsocketEventBuilder {
    }
}
