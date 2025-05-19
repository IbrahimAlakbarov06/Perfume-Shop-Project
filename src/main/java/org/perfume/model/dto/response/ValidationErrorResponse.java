<<<<<<<< HEAD:src/main/java/org/perfume/dto/response/ValidationErrorResponse.java
package org.perfume.dto.response;
========
package org.perfume.model.dto.response;
>>>>>>>> 37e4850 (packages updated):src/main/java/org/perfume/model/dto/response/ValidationErrorResponse.java

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private Map<String, String> errors;
    private int status;
}