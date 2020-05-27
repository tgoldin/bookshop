package com.tpg.bookshop.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewBookRequest {
    private String title;
    private String description;
    private String isbn;
}
