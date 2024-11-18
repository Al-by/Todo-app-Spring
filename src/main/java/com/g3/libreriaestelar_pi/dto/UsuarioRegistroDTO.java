package com.g3.libreriaestelar_pi.dto;

import lombok.Data;

@Data
public class UsuarioRegistroDTO {

    private String username;
    private String email;
    private String password;
    private String dni;
}