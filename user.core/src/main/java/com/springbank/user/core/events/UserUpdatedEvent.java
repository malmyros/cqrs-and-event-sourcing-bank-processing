package com.springbank.user.core.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.springbank.user.core.models.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatedEvent {

    private String id;

    private User user;
}
