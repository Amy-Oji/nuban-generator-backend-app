package com.amyojiakor.nubangeneratorapp.services;

import com.amyojiakor.nubangeneratorapp.models.payloads.NubanGeneratorPayload;
import com.amyojiakor.nubangeneratorapp.models.payloads.NubanGeneratorResponse;

public interface NubanGeneratorService {
    NubanGeneratorResponse generateNuban(NubanGeneratorPayload payload);
}
