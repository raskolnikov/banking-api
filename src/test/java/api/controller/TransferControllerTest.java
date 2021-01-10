package api.controller;

import api.db.model.Transfer;
import api.request.NewTransferRequest;
import api.response.TransferDto;
import api.service.TransferService;
import api.util.TransferMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Mehmet Aktas on 2020-12-11
 */

@WebMvcTest(TransferController.class)
public class TransferControllerTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransferService transferService;

	@MockBean
	private TransferMapper transferMapper;

	private String transferControllerPath = "/api/v1/transfers/";

	@Autowired
	private ObjectMapper objectMapper;


	@Test
	void createTransfer_whenValidTransfersDetailsAreProvidedThenItShouldCreateTransfer() throws Exception {

		NewTransferRequest newTransferRequest = new NewTransferRequest();

		newTransferRequest.setAmount(100L);
		newTransferRequest.setDestinationAccountId(33L);
		newTransferRequest.setAccountId(22L);
		newTransferRequest.setDescription("This is my test transfer");

		Transfer expected = mock(Transfer.class);

		when(transferService.createTransfer(any(NewTransferRequest.class))).thenReturn(expected);

		TransferDto transferDto = new TransferDto();
		transferDto.setId(22L);

		when(transferMapper.convertToTransferDto(expected)).thenReturn(transferDto);

		MvcResult mvcResult = this.mockMvc.perform(post(transferControllerPath).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(newTransferRequest))).andExpect(status().isOk()).andReturn();

		String content = mvcResult.getResponse().getContentAsString();
		TransferDto actualTransfer = objectMapper.readValue(content, TransferDto.class);

		assertThat(actualTransfer.getId()).isEqualTo(transferDto.getId());

		ArgumentCaptor<NewTransferRequest> argumentCaptor = ArgumentCaptor.forClass(NewTransferRequest.class);

		verify(transferService, times(1)).createTransfer(argumentCaptor.capture());

		assertThat(newTransferRequest).usingRecursiveComparison().isEqualTo(argumentCaptor.getValue());

		verifyNoMoreInteractions(transferService);

	}


}
