package com.premierLeague.premier_zone.Security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    public String username;
    public String password;
}
