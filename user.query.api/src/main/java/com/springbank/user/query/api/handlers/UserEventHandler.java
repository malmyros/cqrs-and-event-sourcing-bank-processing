package com.springbank.user.query.api.handlers;

import com.springbank.user.core.events.UserRegisteredEvent;
import com.springbank.user.core.events.UserRemovedEvent;
import com.springbank.user.core.events.UserUpdatedEvent;

public interface UserEventHandler {

    void on(UserRegisteredEvent userRegisteredEvent);

    void on(UserUpdatedEvent userUpdatedEvent);

    void on(UserRemovedEvent userRemovedEvent);
}
