package org.zew.donations.model.request;

import lombok.*;
import org.zew.donations.model.Operation;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WalletUpdateRequest {

    private BigDecimal availableAmount;

    private BigDecimal totalAmount;

    @NotNull
    private Operation operation;

}
