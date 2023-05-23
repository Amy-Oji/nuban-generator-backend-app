package com.amyojiakor.nubangeneratorapp;

import com.amyojiakor.nubangeneratorapp.models.payloads.NubanGeneratorPayload;
import com.amyojiakor.nubangeneratorapp.models.payloads.NubanGeneratorResponse;
import com.amyojiakor.nubangeneratorapp.repositories.NubanRepository;
import com.amyojiakor.nubangeneratorapp.services.NubanGeneratorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class NubanGeneratorAppApplicationTests {

	@Mock
	private NubanRepository nubanRepository;

	@InjectMocks
	private NubanGeneratorServiceImpl nubanGeneratorService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGenerateNuban_ValidInput_ReturnsResponse() throws Exception {
		String bankCode = "057";
		String serialNum = "123456789";
		NubanGeneratorPayload payload = new NubanGeneratorPayload(bankCode, serialNum);
		String expectedNuban = "1234567899";

		NubanGeneratorResponse response = nubanGeneratorService.generateNuban(payload);

		Assertions.assertEquals(expectedNuban, response.generatedNUBAN());
	}

	@Test
	public void testGenerateNuban_lessThan9DigitsSerialNum_ReturnsPaddedSerialNum() throws Exception {
		// Arrange
		String bankCode = "044";
		String serialNum = "61"; //less than 9 digits serial number
		NubanGeneratorPayload payload = new NubanGeneratorPayload(bankCode, serialNum);
		String expectedSerialNum = "000000061";

		NubanGeneratorResponse response = nubanGeneratorService.generateNuban(payload);

		Assertions.assertEquals(9, response.serialNumber().length());

		Assertions.assertEquals(expectedSerialNum, response.serialNumber());

	}

	@Test
	public void testGenerateNuban_InvalidSerialNum_ReturnsErrorResponse() throws Exception {
		// Arrange
		String bankCode = "057";
		String serialNum = ""; // Invalid input: empty serial number
		NubanGeneratorPayload payload = new NubanGeneratorPayload(bankCode, serialNum);

		Assertions.assertThrows(Exception.class, () -> {
			nubanGeneratorService.generateNuban(payload);
		});	}

	@Test
	public void testGenerateNuban_InvalidBanksCode_ReturnsErrorResponse() throws Exception {
		// Arrange
		String bankCode = "000"; // Invalid input: invalid bank code
		String serialNum = "1234";
		NubanGeneratorPayload payload = new NubanGeneratorPayload(bankCode, serialNum);

		Assertions.assertThrows(Exception.class, () -> {
			nubanGeneratorService.generateNuban(payload);
		});	}
}
