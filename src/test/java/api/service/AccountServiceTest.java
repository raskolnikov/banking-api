package api.service;

import api.db.model.Account;
import api.db.model.Customer;
import api.db.repository.AccountRepository;
import api.enums.TransactionType;
import api.exception.NotFoundException;
import api.request.NewAccountRequest;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Mehmet Aktas on 2020-12-10
 */

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private BalanceTransactionService balanceTransactionService;

	@InjectMocks
	private AccountService accountService;

	@Test
	void createAccount_whenValidRequestIsGivenThenItShouldCreateNewAccount() {

		NewAccountRequest newAccountRequest = new NewAccountRequest();

		newAccountRequest.setAccountName("account1");
		newAccountRequest.setCustomerId(1L);
		newAccountRequest.setInitialBalance(100L);

		Account createdAccount = new Account();

		createdAccount.setId(11L);
		createdAccount.setName(newAccountRequest.getAccountName());

		Customer customer = new Customer();
		customer.setId(newAccountRequest.getCustomerId());

		createdAccount.setCustomer(customer);
		createdAccount.setBalance(newAccountRequest.getInitialBalance());

		when(accountRepository.save(any(Account.class))).thenReturn(createdAccount);

		Account actualAccount = accountService.createAccount(newAccountRequest);

		assertThat(actualAccount).isEqualTo(createdAccount);

		// Actual Call
		ArgumentCaptor<Account> argumentCaptorAccount = ArgumentCaptor.forClass(Account.class);

		verify(accountRepository, times(1)).save(argumentCaptorAccount.capture());

		Account capturedAccount = argumentCaptorAccount.getValue();

		assertThat(capturedAccount.getId()).isNull();
		assertThat(capturedAccount.getCustomer().getId()).isEqualTo(newAccountRequest.getCustomerId());
		assertThat(capturedAccount.getName()).isEqualTo(newAccountRequest.getAccountName());
		assertThat(capturedAccount.getBalance()).isEqualTo(newAccountRequest.getInitialBalance());

		verify(balanceTransactionService, times(1)).createBalanceTransactionAndSetAccount(eq(createdAccount.getId()),
				eq(newAccountRequest.getInitialBalance()),
				eq(0L),
				eq("Initial deposit"),
				eq(TransactionType.DEPOSIT),
				isNull());

		verifyNoMoreInteractions(accountRepository, balanceTransactionService);

	}

	@Test
	void getAccount_whenInvalidAccountIdIsGivenThenItShouldThrowException() {

		Long accountId = 11L;

		when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(null));

		Throwable thrown = catchThrowable(() -> {

			accountService.getAccount(accountId);

		});

		assertThat(thrown).isInstanceOf(NotFoundException.class).hasMessageContaining("Account not found");

		verifyNoMoreInteractions(accountRepository);

	}

	@Test
	void getAccount_whenValidAccountIdIsGivenThenItShouldReturnAccount() {

		Long accountId = 11L;

		Account expected = new Account();

		expected.setId(accountId);
		expected.setName("My test account");


		when(accountRepository.findById(accountId)).thenReturn(Optional.of(expected));

		Account actualAccount = accountService.getAccount(accountId);

		assertThat(actualAccount).isEqualTo(expected);
	}


}