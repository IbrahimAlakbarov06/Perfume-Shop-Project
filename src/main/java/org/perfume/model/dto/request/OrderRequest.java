package org.perfume.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotBlank(message = "WhatsApp number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid WhatsApp number format")
    private String whatsappNumber;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    private String customerNotes;
}