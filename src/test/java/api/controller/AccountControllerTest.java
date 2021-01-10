package api.controller;

import api.db.model.Account;
import api.db.model.BalanceTransaction;
import api.db.model.Transfer;
import api.request.NewAccountRequest;
import api.response.AccountDto;
import api.response.BalanceTransactionDto;
import api.response.TransferDto;
import api.service.AccountService;
import api.service.BalanceTransactionService;
import api.service.TransferService;
import api.util.AccountMapper;
import api.util.BalanceTransactionMapper;
import api.util.TransferMapper;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Mehmet Aktas on 2020-12-10
 */

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	@MockBean
	private BalanceTransactionMapper balanceTransactionMapper;

	@MockBean
	private AccountMapper accountMapper;

	@MockBean
	private TransferMapper transferMapper;

	@MockBean
	private BalanceTransactionService balanceTransactionService;

	@MockBean
	private TransferService transferService;


	private String accountControllerPath = "/api/v1/accounts/";

	@Autowired
	private ObjectMapper objectMapper;


	@Test
	void createAccount_whenValidAccountsDetailsAreProvidedThenItShouldCreateAccount() throws Exception {

		NewAccountRequest newAccountRequest = new NewAccountRequest();

		newAccountRequest.setAccountName("Account1");
		newAccountRequest.setCustomerId(1L);
		newAccountRequest.setInitialBalance(100L);

		Account expectedAccount = mock(Account.class);

		AccountDto accountDto = new AccountDto();
		accountDto.setCustomerId(newAccountRequest.getCustomerId());

		when(accountService.createAccount(any(NewAccountRequest.class))).thenReturn(expectedAccount);
		when(accountMapper.convertToAccountDto(expectedAccount)).thenReturn(accountDto);

		MvcResult mvcResult = this.mockMvc.perform(post(accountControllerPath).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(newAccountRequest))).andExpect(status().isOk()).andReturn();

		String content = mvcResult.getResponse().getContentAsString();
		AccountDto actualAccount = objectMapper.readValue(content, AccountDto.class);

		assertThat(actualAccount.getCustomerId()).isEqualTo(newAccountRequest.getCustomerId());

		ArgumentCaptor<NewAccountRequest> argumentCaptor = ArgumentCaptor.forClass(NewAccountRequest.class);

		verify(accountService, times(1)).createAccount(argumentCaptor.capture());

		assertThat(newAccountRequest).usingRecursiveComparison().isEqualTo(argumentCaptor.getValue());

		verifyNoMoreInteractions(accountService);

	}


	@Test
	void getAccount_whenValidAccountIdIsGivenThenItShouldReturnAccount() throws Exception {

		Long accountId = 22L;

		Account account = mock(Account.class);

		when(accountService.getAccount(accountId)).thenReturn(account);

		AccountDto expectedAccountDto = new AccountDto();
		expectedAccountDto.setId(accountId);

		when(accountMapper.convertToAccountDto(account)).thenReturn(expectedAccountDto);

		MvcResult mvcResult = this.mockMvc.perform(get(accountControllerPath + "/{id}",
				accountId).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		String content = mvcResult.getResponse().getContentAsString();
		AccountDto actualAccountDto = objectMapper.readValue(content, AccountDto.class);

		assertThat(actualAccountDto).usingRecursiveComparison().isEqualTo(expectedAccountDto);

	}

	@Test
	void getAccountTransfers_whenValidParamsAreGivenThenItShouldReturnTransferList() throws Exception {

		Long accountId = 22L;
		Integer offset = 0;
		Integer limit = 3;

		Account account = mock(Account.class);

		when(accountService.getAccount(accountId)).thenReturn(account);

		Transfer transfer = mock(Transfer.class);
		List<Transfer> transferList = Collections.nCopies(limit, transfer);

		when(transferService.getAccountTransfers(accountId, offset, limit)).thenReturn(transferList);

		TransferDto transferDto = new TransferDto();
		transferDto.setId(12L);

		List<TransferDto> expectedTransferDtoList = Collections.nCopies(limit, transferDto);

		when(transferMapper.convertToTransferDto(transfer)).thenReturn(transferDto);

		MvcResult mvcResult = this.mockMvc.perform(get(accountControllerPath + "/{id}/transfers",
				accountId).contentType(MediaType.APPLICATION_JSON)
				.param("offset", offset.toString())
				.param("limit", limit.toString())).andExpect(status().isOk()).andReturn();

		String content = mvcResult.getResponse().getContentAsString();
		List<TransferDto> actualTransfers = objectMapper.readValue(content, new TypeReference<List<TransferDto>>() {
		});

		assertThat(actualTransfers).usingFieldByFieldElementComparator().containsAll(expectedTransferDtoList);


	}


	@Test
	void getAccountBalanceTransactions_whenValidParamsAreGivenThenItShouldReturnBalanceTransactionList() throws Exception {

		Long accountId = 22L;
		Integer offset = 0;
		Integer limit = 3;

		Account account = mock(Account.class);

		when(accountService.getAccount(accountId)).thenReturn(account);

		BalanceTransaction transfer = mock(BalanceTransaction.class);
		List<BalanceTransaction> balanceTransactionList = Collections.nCopies(limit, transfer);

		when(balanceTransactionService.getAccountBalanceTransactions(accountId, offset, limit)).thenReturn(
				balanceTransactionList);

		BalanceTransactionDto balanceTransactionDto = new BalanceTransactionDto();
		balanceTransactionDto.setId(12L);

		List<BalanceTransactionDto> expectedBalanceTransactionDtoList = Collections.nCopies(limit,
				balanceTransactionDto);

		when(balanceTransactionMapper.convertToBalanceTransactionDto(transfer)).thenReturn(balanceTransactionDto);

		MvcResult mvcResult = this.mockMvc.perform(get(accountControllerPath + "/{id}/balance-transactions",
				accountId).contentType(MediaType.APPLICATION_JSON)
				.param("offset", offset.toString())
				.param("limit", limit.toString())).andExpect(status().isOk()).andReturn();

		String content = mvcResult.getResponse().getContentAsString();
		List<BalanceTransactionDto> actualBalanceTransactionDtoList = objectMapper.readValue(content,
				new TypeReference<List<BalanceTransactionDto>>() {
				});

		assertThat(actualBalanceTransactionDtoList).usingFieldByFieldElementComparator()
				.containsAll(expectedBalanceTransactionDtoList);


	}


}
