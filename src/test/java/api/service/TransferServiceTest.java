package api.service;

import api.db.model.Account;
import api.db.model.BalanceTransaction;
import api.db.model.Transfer;
import api.db.repository.AccountRepository;
import api.db.repository.BalanceTransactionRepository;
import api.db.repository.TransferRepository;
import api.enums.TransactionType;
import api.exception.InvalidRequestParamException;
import api.exception.NotFoundException;
import api.request.NewTransferRequest;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Mehmet Aktas on 2020-12-11
 */

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {


	@Mock
	private AccountRepository accountRepository;

	@Mock
	private BalanceTransactionRepository balanceTransactionRepository;

	@Mock
	private BalanceTransactionService balanceTransactionService;

	@Mock
	private TransferRepository transferRepository;

	@Captor
	private ArgumentCaptor<List<BalanceTransaction>> balanceTransactionListCaptor;

	@Captor
	private ArgumentCaptor<List<Account>> accountListCaptor;

	@InjectMocks
	private TransferService transferService;

	private NewTransferRequest newTransferRequest;
	private Account sourceAccount;
	private Account destinationAccount;


	@BeforeEach
	void beforeEach() {

		newTransferRequest = new NewTransferRequest();

		newTransferRequest.setAccountId(22L);
		newTransferRequest.setAmount(100L);
		newTransferRequest.setDescription("My testing transfer");
		newTransferRequest.setDestinationAccountId(33L);

		sourceAccount = new Account();
		sourceAccount.setId(newTransferRequest.getAccountId());
		sourceAccount.setBalance(200L);

		destinationAccount = new Account();
		destinationAccount.setId(newTransferRequest.getDestinationAccountId());
		destinationAccount.setBalance(0L);

	}

	@Test
	void createTransfer_whenInvalidSourceAccountIsGivenThenItShouldThrowException() {

		when(accountRepository.findById(newTransferRequest.getAccountId())).thenReturn(Optional.ofNullable(null));

		Throwable thrown = catchThrowable(() -> {

			transferService.createTransfer(newTransferRequest);

		});

		assertThat(thrown).isInstanceOf(NotFoundException.class).hasMessageContaining("Account not found");

		verifyNoMoreInteractions(accountRepository);
		verifyNoInteractions(balanceTransactionRepository, transferRepository);

	}

	@Test
	void createTransfer_whenInvalidDestinationAccountIsGivenThenItShouldThrowException() {

		when(accountRepository.findById(newTransferRequest.getAccountId())).thenReturn(Optional.of(sourceAccount));

		when(accountRepository.findById(newTransferRequest.getDestinationAccountId())).thenReturn(Optional.ofNullable(
				null));

		Throwable thrown = catchThrowable(() -> {

			transferService.createTransfer(newTransferRequest);

		});

		assertThat(thrown).isInstanceOf(NotFoundException.class).hasMessageContaining("Destination account not found");

		verifyNoMoreInteractions(accountRepository);
		verifyNoInteractions(balanceTransactionRepository, transferRepository);

	}


	@Test
	void createTransfer_whenTransferAmountLessThanOneIsGivenThenItShouldThrowException() {

		newTransferRequest.setAmount(0L);

		Throwable thrown = catchThrowable(() -> {

			transferService.createTransfer(newTransferRequest);

		});

		assertThat(thrown).isInstanceOf(InvalidRequestParamException.class)
				.hasMessageContaining("Transfer amount has to be at least 1 pence");

		verifyNoMoreInteractions(accountRepository);
		verifyNoInteractions(balanceTransactionRepository, transferRepository);

	}

	@Test
	void createTransfer_whenValidTransferParamsAreGivenThenItShouldMakeTransferSuccessfully() {

		when(accountRepository.findById(newTransferRequest.getAccountId())).thenReturn(Optional.of(sourceAccount));
		when(accountRepository.findById(newTransferRequest.getDestinationAccountId())).thenReturn(Optional.of(
				destinationAccount));

		Transfer createdTransfer = new Transfer();

		createdTransfer.setId(11L);
		createdTransfer.setDestinationAccountId(newTransferRequest.getDestinationAccountId());
		createdTransfer.setDescription(newTransferRequest.getDescription());
		createdTransfer.setAmount(newTransferRequest.getAmount());
		createdTransfer.setAccountId(newTransferRequest.getAccountId());

		when(transferRepository.save(any(Transfer.class))).thenReturn(createdTransfer);

		// Method call
		Transfer actualTransfer = transferService.createTransfer(newTransferRequest);

		assertThat(actualTransfer).isEqualTo(createdTransfer);

		// Balance adjustment for source account
		verify(balanceTransactionService, times(1)).createBalanceTransactionAndSetAccount(eq(sourceAccount.getId()),
				eq(-1 * newTransferRequest.getAmount()),
				eq(sourceAccount.getBalance()),
				eq(newTransferRequest.getDescription()),
				eq(TransactionType.TRANSFER),
				eq(createdTransfer.getId()));

		// Balance adjustment for destination account
		verify(balanceTransactionService,
				times(1)).createBalanceTransactionAndSetAccount(eq(destinationAccount.getId()),
				eq(newTransferRequest.getAmount()),
				eq(destinationAccount.getBalance()),
				eq(newTransferRequest.getDescription()),
				eq(TransactionType.TRANSFER),
				eq(createdTransfer.getId()));

		verifyNoMoreInteractions(accountRepository, transferRepository, balanceTransactionService);

	}

	@Test
	void getAccountTransfers_whenValidFilterParamsAreGivenThenItShouldReturnList() {

		Long accountId = 22L;
		Integer offset = 0;
		Integer limit = 2;

		Transfer balanceTransaction = mock(Transfer.class);

		List<Transfer> transferList = Collections.nCopies(2, balanceTransaction);

		when(transferRepository.findInOutTransfersByAccountId(eq(accountId), any(PageRequest.class))).thenReturn(
				transferList);

		List<Transfer> accountTransfers = transferService.getAccountTransfers(
				accountId,
				offset,
				limit);

		assertThat(accountTransfers).containsExactlyElementsOf(transferList);

	}


}