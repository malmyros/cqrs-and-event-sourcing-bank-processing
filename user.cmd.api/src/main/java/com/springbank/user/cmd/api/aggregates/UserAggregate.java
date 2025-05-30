package com.springbank.user.cmd.api.aggregates;

import com.springbank.user.cmd.api.commands.RegisterUserCommand;
import com.springbank.user.cmd.api.commands.RemoveUserCommand;
import com.springbank.user.cmd.api.commands.UpdateUserCommand;
import com.springbank.user.cmd.api.security.PasswordEncoder;
import com.springbank.user.cmd.api.security.PasswordEncoderImpl;
import events.UserRegisteredEvent;
import events.UserRemovedEvent;
import events.UserUpdatedEvent;
import models.User;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate
public class UserAggregate {

    @AggregateIdentifier
    private String id;

    private User user;

    private final PasswordEncoder passwordEncoder;

    public UserAggregate() {
        this.passwordEncoder = new PasswordEncoderImpl();
    }

    @CommandHandler
    public UserAggregate(RegisterUserCommand registerUserCommand) {
        User newUser = registerUserCommand.getUser();
        newUser.setId(registerUserCommand.getId());

        this.passwordEncoder = new PasswordEncoderImpl();

        String password = newUser.getAccount().getPassword();
        String hashedPassword = passwordEncoder.hashPassword(password);
        newUser.getAccount().setPassword(hashedPassword);

        UserRegisteredEvent userRegisteredEvent = UserRegisteredEvent.builder()
                .id(registerUserCommand.getId())
                .user(newUser)
                .build();

        AggregateLifecycle.apply(userRegisteredEvent);
    }

    @CommandHandler
    public void handle(UpdateUserCommand updateUserCommand) {
        User updatedUser = updateUserCommand.getUser();
        updatedUser.setId(updateUserCommand.getId());

        String password = updatedUser.getAccount().getPassword();
        String hashedPassword = passwordEncoder.hashPassword(password);
        updatedUser.getAccount().setPassword(hashedPassword);

        UserUpdatedEvent userUpdatedEvent = UserUpdatedEvent.builder()
                .id(UUID.randomUUID().toString())
                .user(updatedUser)
                .build();

        AggregateLifecycle.apply(userUpdatedEvent);
    }

    @CommandHandler
    public void handle(RemoveUserCommand removeUserCommand) {
        UserRemovedEvent userRemovedEvent = new UserRemovedEvent();
        userRemovedEvent.setId(removeUserCommand.getId());

        AggregateLifecycle.apply(userRemovedEvent);
    }

    @EventSourcingHandler
    public void on(UserRegisteredEvent userRegisteredEvent) {
        this.id = userRegisteredEvent.getId();
        this.user = userRegisteredEvent.getUser();
    }

    @EventSourcingHandler
    public void on(UserUpdatedEvent userUpdatedEvent) {
        this.user = userUpdatedEvent.getUser();
    }

    @EventSourcingHandler
    public void on(UserRemovedEvent userRemovedEvent) {
        AggregateLifecycle.markDeleted();
    }
}
