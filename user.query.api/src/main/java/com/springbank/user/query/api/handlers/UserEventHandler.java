package com.springbank.user.query.api.handlers;

import events.UserRegisteredEvent;
import events.UserRemovedEvent;
import events.UserUpdatedEvent;

public interface UserEventHandler {

    void on(UserRegisteredEvent userRegisteredEvent);

    void on(UserUpdatedEvent userUpdatedEvent);

    void on(UserRemovedEvent userRemovedEvent);
}
