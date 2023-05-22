package com.amyojiakor.nubangeneratorapp.services;

import com.amyojiakor.nubangeneratorapp.models.payloads.NubanGeneratorPayload;
import com.amyojiakor.nubangeneratorapp.models.payloads.NubanGeneratorResponse;

import java.io.IOException;

public interface NubanGeneratorService {
    NubanGeneratorResponse generateNuban(NubanGeneratorPayload payload) throws Exception;
}
