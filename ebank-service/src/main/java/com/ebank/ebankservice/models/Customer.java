package com.ebank.ebankservice.models;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {
    private Long id;
    private String name;
    private String email;
}
