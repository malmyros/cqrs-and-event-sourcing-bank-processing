package com.springbank.user.query.api.controllers;

import com.springbank.user.query.api.dto.UserLookupResponse;
import com.springbank.user.query.api.queries.FindAllUsersQuery;
import com.springbank.user.query.api.queries.FindUserByIdQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/user-lookup")
public class UserLookupController {

    private final QueryGateway queryGateway;

    @Autowired
    public UserLookupController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public ResponseEntity<UserLookupResponse> findAllUsers() {

        try {

            FindAllUsersQuery findAllUsersQuery = new FindAllUsersQuery();
            UserLookupResponse userLookupResponse = queryGateway
                    .query(findAllUsersQuery, ResponseTypes.instanceOf(UserLookupResponse.class))
                    .join();

            return userLookupResponse == null
                    ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
                    : new ResponseEntity<>(userLookupResponse, HttpStatus.OK);

        } catch (Exception e) {

            String message = "Failed to complete get all users request";
            log.error(message, e);
            return new ResponseEntity<>(new UserLookupResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserLookupResponse> findUserById(
            @PathVariable(value = "id") String id) {

        try {

            FindUserByIdQuery findUserByIdQuery = new FindUserByIdQuery(id);
            UserLookupResponse userLookupResponse = queryGateway
                    .query(findUserByIdQuery, ResponseTypes.instanceOf(UserLookupResponse.class))
                    .join();

            return userLookupResponse == null
                    ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
                    : new ResponseEntity<>(userLookupResponse, HttpStatus.OK);

        } catch (Exception e) {

            String message = "Failed to complete get all users request";
            log.error(message, e);
            return new ResponseEntity<>(new UserLookupResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
