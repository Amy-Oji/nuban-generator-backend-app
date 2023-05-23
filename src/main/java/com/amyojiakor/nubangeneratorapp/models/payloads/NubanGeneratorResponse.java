package com.amyojiakor.nubangeneratorapp.models.payloads;

import java.time.LocalDateTime;

public record NubanGeneratorResponse(String generatedNUBAN, String serialNumber, BankDataDto bankData, LocalDateTime dateTime) {
}
