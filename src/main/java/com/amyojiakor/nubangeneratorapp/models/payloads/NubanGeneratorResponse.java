package com.amyojiakor.nubangeneratorapp.models.payloads;

import java.time.LocalDateTime;

public record NubanGeneratorResponse(String bankCode, String serialNum, String generatedNuban, LocalDateTime dateTime) {
}
