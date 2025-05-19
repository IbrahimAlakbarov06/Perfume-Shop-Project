<<<<<<<< HEAD:src/main/java/org/perfume/dto/response/BrandResponse.java
package org.perfume.dto.response;
========
package org.perfume.model.dto.response;
>>>>>>>> 37e4850 (packages updated):src/main/java/org/perfume/model/dto/response/BrandDto.java

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private Long id;
    private String name;
    private String description;
    private String logoUrl;
}