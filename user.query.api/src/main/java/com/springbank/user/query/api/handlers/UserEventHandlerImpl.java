package com.springbank.user.query.api.handlers;

import com.springbank.user.query.api.repositories.UserRepository;
import com.springbank.user.core.events.UserRegisteredEvent;
import com.springbank.user.core.events.UserRemovedEvent;
import com.springbank.user.core.events.UserUpdatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("user-group")
public class UserEventHandlerImpl implements UserEventHandler {

    private final UserRepository userRepository;

    @Autowired
    public UserEventHandlerImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventHandler
    @Override
    public void on(UserRegisteredEvent userRegisteredEvent) {
        userRepository.save(userRegisteredEvent.getUser());
    }

    @EventHandler
    @Override
    public void on(UserUpdatedEvent userUpdatedEvent) {
        userRepository.save(userUpdatedEvent.getUser());
    }

    @EventHandler
    @Override
    public void on(UserRemovedEvent userRemovedEvent) {
        userRepository.deleteById(userRemovedEvent.getId());
    }
}