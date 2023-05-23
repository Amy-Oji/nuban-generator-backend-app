package com.amyojiakor.nubangeneratorapp.services;

import com.amyojiakor.nubangeneratorapp.models.entities.NubanEntity;
import com.amyojiakor.nubangeneratorapp.models.payloads.BankDataDto;
import com.amyojiakor.nubangeneratorapp.models.payloads.NubanGeneratorPayload;
import com.amyojiakor.nubangeneratorapp.models.payloads.NubanGeneratorResponse;
import com.amyojiakor.nubangeneratorapp.repositories.NubanRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class NubanGeneratorServiceImpl implements NubanGeneratorService{
    private static final long [] NUBAN_MULTIPLIERS = {3,7,3,3,7,3,3,7,3,3,7,3};
    private static final int MODULO_VALUE = 10;
    private static final long SERIAL_NUMBER_LENGTH = 9L;
    @Autowired
    private final NubanRepository nubanRepository;

    /**
     * Generates a NUBAN (Nigeria Uniform Bank Account Number) based on the provided payload.
     *
     * @param payload The payload containing the bank code and serial number.
     * @return A NubanGeneratorResponse object representing the generated NUBAN,
     * including additional information such as the bank data and date/time.
     * @throws Exception If the serial number is invalid (more than 9 digits or empty).
     */
    @Override
    public NubanGeneratorResponse generateNuban(NubanGeneratorPayload payload) throws Exception{
        
        String serialNumber = payload.serialNumber();
        String bankCode = payload.bankCode();

        if (!serialNumber.matches("^[0-9]*$") || !bankCode.matches("^[0-9]*$")) {
            throw new Exception("values must contain only digits");
        }

        BankDataDto bank = getBankData(bankCode);
        
        if(serialNumber.length() > SERIAL_NUMBER_LENGTH || serialNumber.isEmpty()){
            throw new Exception("Serial Number should not be more than 9 digits and should not be empty");
        }

        serialNumber = serialNumber.length()<SERIAL_NUMBER_LENGTH ? padSerialNumber(serialNumber) : serialNumber;

        long checkDigit = calculateCheckDigit(bankCode, serialNumber);

        String generatedNuban = serialNumber + checkDigit;

        return setNubanEntityAndResponse(bank, serialNumber, generatedNuban);
    }

    /**
     * Retrieves the bank data for the given bank code.
     *
     * @param bankCode The bank code used to identify the bank.
     * @return The BankDataDto object representing the bank information.
     * @throws IOException If there is an error reading the bank list or if the bank code is invalid.
     */
    private BankDataDto getBankData(String bankCode) throws IOException {

        List<BankDataDto> bankCodesList = getBankAndCbnCodes();

        return bankCodesList.stream().filter(b -> b.uniqueCbnBankCode()
                        .equals(bankCode))
                .findFirst()
                .orElseThrow(() -> new IOException("Invalid Bank Code. Please enter a valid bank code"));
    }

    private List<BankDataDto> getBankAndCbnCodes() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(
                new File("src/main/resources/bank_list.json"),
                new TypeReference<>() {
                }
        );
    }

    /**
     * Pads the given serial number with leading zeros to ensure it has a fixed length.
     *
     * @param serialNumber The serial number to be padded.
     * @return The padded serial number as a string.
     */
    private String padSerialNumber(String serialNumber) {

        StringBuilder paddedSerialNumber = new StringBuilder();
        paddedSerialNumber.append(serialNumber);

        while (paddedSerialNumber.length() < SERIAL_NUMBER_LENGTH) {
            paddedSerialNumber.insert(0, 0);
        }

        return paddedSerialNumber.toString();
    }

    /**
     * Calculates the check digit for generating the NUBAN (Nigeria Uniform Bank Account Number)
     * following the CBN provided formula: A*+B*7+C*3+D*3+E*7+F*3+G*3+H*7+I*3+J*3+K*7+L*3
     * then divide the sum by 10
     * then subtract the sum from 10
     * if what is left is 10, then 0 is the check digit. Else, what is left is now the check digit.
     *
     * @param bankCode      The bank code associated with the NUBAN.
     * @param serialNumber  The serial number used to generate the NUBAN.
     * @return The calculated check digit for the NUBAN.
     */
    private long calculateCheckDigit(String bankCode, String serialNumber){

        String bankCodeAndSerialNumber =  bankCode + serialNumber;

        var splitBankCodeAndSerialNumber  = bankCodeAndSerialNumber.toCharArray();

        long sum = IntStream.range(0, splitBankCodeAndSerialNumber.length)
                .mapToLong(i -> (splitBankCodeAndSerialNumber[i] - '0') * NUBAN_MULTIPLIERS[i])
                .sum();

        sum = sum % MODULO_VALUE;

        return MODULO_VALUE - sum == MODULO_VALUE ? 0 : MODULO_VALUE - sum;
    }

    /**
     * Sets the NUBAN entity and creates a response object.
     *
     * @param bank            The bank data associated with the NUBAN.
     * @param serialNum       The serial number used to generate the NUBAN.
     * @param generatedNuban  The generated NUBAN.
     * @return The NubanGeneratorResponse containing the generated NUBAN, serial number, bank data, and date/time.
     */
    private NubanGeneratorResponse setNubanEntityAndResponse (BankDataDto bank, String serialNum, String generatedNuban){

        LocalDateTime now = LocalDateTime.now();

        NubanEntity nubanEntity = new NubanEntity();
        nubanEntity.setBankCode(bank.uniqueCbnBankCode());
        nubanEntity.setSerialNum(serialNum);
        nubanEntity.setBankName(bank.bankName());
        nubanEntity.setGeneratedNuban(generatedNuban);
        nubanEntity.setDateTime(now);
        nubanRepository.save(nubanEntity);

        return new NubanGeneratorResponse(generatedNuban, serialNum, bank, now);
    }

}
