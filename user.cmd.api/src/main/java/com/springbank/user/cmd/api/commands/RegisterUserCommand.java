package com.springbank.user.cmd.api.commands;

import lombok.Builder;
import lombok.Data;
import models.User;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class RegisterUserCommand {

    @TargetAggregateIdentifier
    private String id;

    private User user;
}
