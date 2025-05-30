package com.springbank.user.cmd.api.controllers;

import com.springbank.user.cmd.api.commands.RegisterUserCommand;
import com.springbank.user.cmd.api.commands.RemoveUserCommand;
import com.springbank.user.cmd.api.commands.UpdateUserCommand;
import com.springbank.user.cmd.api.dto.BaseResponse;
import com.springbank.user.cmd.api.dto.RegisteredUserResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final CommandGateway commandGateway;

    @Autowired
    public UserController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public ResponseEntity<RegisteredUserResponse> registerUser(
            @RequestBody RegisterUserCommand registerUserCommand) {


        String commandId = UUID.randomUUID().toString();
        registerUserCommand.setId(commandId);

        try {

            commandGateway.sendAndWait(registerUserCommand, 1, TimeUnit.SECONDS);
            String message = "User successfully registered";
            return new ResponseEntity<>(new RegisteredUserResponse(commandId, message), HttpStatus.CREATED);

        } catch (Exception e) {
            String message = "Error while processing register user request for commandId " + commandId;
            return new ResponseEntity<>(new RegisteredUserResponse(commandId, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> updateUser(
            @PathVariable(value = "id") String id,
            @RequestBody UpdateUserCommand updateUserCommand) {

        try {

            updateUserCommand.setId(id);
            commandGateway.sendAndWait(updateUserCommand);

            String message = "User successfully updated";
            return new ResponseEntity<>(new BaseResponse(message), HttpStatus.CREATED);

        } catch (Exception e) {
            String message = "Error while processing update user request for commandId " + id;
            return new ResponseEntity<>(new BaseResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> removeUser(
            @PathVariable(value = "id") String id) {

        try {

            RemoveUserCommand removeUserCommand = new RemoveUserCommand(id);
            commandGateway.sendAndWait(removeUserCommand);

            String message = "User successfully removed";
            return new ResponseEntity<>(new BaseResponse(message), HttpStatus.CREATED);

        } catch (Exception e) {
            String message = "Error while processing remove user request for commandId " + id;
            return new ResponseEntity<>(new BaseResponse(message), HttpStatus.NOT_FOUND);
        }
    }
}
