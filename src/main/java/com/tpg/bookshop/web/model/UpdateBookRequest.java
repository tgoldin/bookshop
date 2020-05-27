package com.tpg.bookshop.web.model;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBookRequest {
    private UUID uuid;
    private String title;
    private String description;
    private String isbn;
}
