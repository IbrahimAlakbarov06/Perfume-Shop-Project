<<<<<<<< HEAD:src/main/java/org/perfume/dto/response/ErrorResponse.java
package org.perfume.dto.response;
========
package org.perfume.model.dto.response;
>>>>>>>> 37e4850 (packages updated):src/main/java/org/perfume/model/dto/response/ErrorResponse.java

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private int status;
}