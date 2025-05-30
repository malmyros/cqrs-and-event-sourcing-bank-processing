package com.springbank.user.query.api.handlers;

import com.springbank.user.query.api.dto.UserLookupResponse;
import com.springbank.user.query.api.queries.FindAllUsersQuery;
import com.springbank.user.query.api.queries.FindUserByIdQuery;
import com.springbank.user.query.api.queries.SearchUsersQuery;

public interface UserQueryHandler {

    UserLookupResponse getUserById(FindUserByIdQuery findUserByIdQuery);

    UserLookupResponse searchUsers(SearchUsersQuery searchUsersQuery);

    UserLookupResponse getAllUsers(FindAllUsersQuery findAllUsersQuery);
}
