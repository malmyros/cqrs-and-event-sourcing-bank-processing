package events;

import lombok.Builder;
import lombok.Data;
import models.User;

@Data
@Builder
public class UserUpdatedEvent {

    private String id;

    private User user;
}
