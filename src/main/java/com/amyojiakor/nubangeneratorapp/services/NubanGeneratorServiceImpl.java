package com.amyojiakor.nubangeneratorapp.services;

import com.amyojiakor.nubangeneratorapp.models.payloads.NubanGeneratorPayload;
import com.amyojiakor.nubangeneratorapp.models.payloads.NubanGeneratorResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Service
public class NubanGeneratorServiceImpl implements NubanGeneratorService{
    private static final long [] NUBAN_MULTIPLIERS = {3,7,3,3,7,3,3,7,3,3,7,3};
    private static final int MODULO_VALUE = 10;

    @Override
    public NubanGeneratorResponse generateNuban(NubanGeneratorPayload payload) {

        long checkDigit = calculateNuban(payload.bankCode(), payload.serialNum());

        String generatedNum = payload.serialNum()+checkDigit;

        System.out.println(generatedNum);


        return new NubanGeneratorResponse(payload.bankCode(), payload.serialNum(), generatedNum, LocalDateTime.now());
    }

    private long calculateNuban(String bankCode, String serialNumber){

        String bankCodeAndSerialNumber =  bankCode + serialNumber;

        var splitBankCodeAndSerialNumber  = bankCodeAndSerialNumber.toCharArray();

        long sum = IntStream.range(0, splitBankCodeAndSerialNumber.length)
                .mapToLong(i -> (splitBankCodeAndSerialNumber[i] - '0') * NUBAN_MULTIPLIERS[i])
                .sum();

        long result = sum % MODULO_VALUE;

        sum = 10 - result == 10 ? 0 : 10 - result;

        return sum;
    }
}
