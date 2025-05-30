package com.springbank.user.cmd.api.dto;

import lombok.Getter;

@Getter
public class RegisteredUserResponse extends BaseResponse {

    private String id;

    public RegisteredUserResponse(String id, String message) {
        super(message);
        this.id = id;
    }

}
