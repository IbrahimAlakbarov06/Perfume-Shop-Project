package org.perfume.perfumebackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    private Long id;
    private String name;
    private String description;
    private String country;
    private String foundedYear;
    private String logoUrl;
}