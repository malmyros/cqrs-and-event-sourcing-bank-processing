package events;

import lombok.Builder;
import lombok.Data;
import models.User;

@Data
@Builder
public class UserRegisteredEvent {

    private String id;

    private User user;
}
