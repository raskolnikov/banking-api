package api.service;

import api.db.model.BalanceTransaction;
import api.db.repository.AccountRepository;
import api.db.repository.BalanceTransactionRepository;
import api.enums.TransactionType;
import api.util.BalanceTransactionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Mehmet Aktas on 2020-12-12
 */

@ExtendWith(MockitoExtension.class)
class BalanceTransactionServiceTest {


	@Mock
	private BalanceTransactionMapper balanceTransactionMapper;

	@Mock
	private BalanceTransactionRepository balanceTransactionRepository;

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private BalanceTransactionService balanceTransactionService;


	@Test
	void getAccountBalanceTransactions_whenValidFilterParamsAreGivenThenItShouldReturnList() {

		Long accountId = 22L;
		Integer offset = 0;
		Integer limit = 2;

		BalanceTransaction balanceTransaction = mock(BalanceTransaction.class);

		List<BalanceTransaction> balanceTransactionList = Collections.nCopies(2, balanceTransaction);

		when(balanceTransactionRepository.findByAccountId(eq(accountId), any(PageRequest.class))).thenReturn(
				balanceTransactionList);

		List<BalanceTransaction> actualBalanceTransactionList = balanceTransactionService.getAccountBalanceTransactions(
				accountId,
				offset,
				limit);

		assertThat(actualBalanceTransactionList).containsExactlyElementsOf(balanceTransactionList);

	}


	@Test
	void createBalanceTransactionAndSetAccount_whenValidParamsAreGivenThenItShouldCreateBalanceTransactionAndSetAccountBalance() {

		Long accountId = 22L;
		Long amount = 20L;
		Long balanceBefore = 300L;
		String description = "My test transfer";
		TransactionType sourceType = TransactionType.TRANSFER;
		Long sourceId = 33L;

		balanceTransactionService.createBalanceTransactionAndSetAccount(accountId,
				amount,
				balanceBefore,
				description,
				sourceType,
				sourceId);


		ArgumentCaptor<BalanceTransaction> balanceTransactionArgumentCaptor = ArgumentCaptor.forClass(BalanceTransaction.class);

		verify(balanceTransactionRepository, times(1)).save(balanceTransactionArgumentCaptor.capture());

		Long balanceAfter = balanceBefore + amount;

		verify(accountRepository, times(1)).updateAccountBalance(accountId, balanceAfter);

		verifyNoMoreInteractions(balanceTransactionRepository, accountRepository);
	}
}