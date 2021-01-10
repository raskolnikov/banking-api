package api.service;

import api.db.model.BalanceTransaction;
import api.db.repository.AccountRepository;
import api.db.repository.BalanceTransactionRepository;
import api.enums.TransactionType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Mehmet Aktas on 2020-12-12
 * <p>
 * This service class will be doing balance transaction operations
 */


@Service
public class BalanceTransactionService {

	private final BalanceTransactionRepository balanceTransactionRepository;
	private final AccountRepository accountRepository;

	@Autowired
	public BalanceTransactionService(BalanceTransactionRepository balanceTransactionRepository,
			AccountRepository accountRepository) {

		this.balanceTransactionRepository = balanceTransactionRepository;

		this.accountRepository = accountRepository;
	}

	/**
	 * This method will return balance transactions for given filtering params
	 *
	 * @param accountId
	 * 		account id
	 * @param offset
	 * 		starting point of list
	 * @param limit
	 * 		size of returning list
	 * @return balance transactions
	 */
	public List<BalanceTransaction> getAccountBalanceTransactions(Long accountId, Integer offset, Integer limit) {

		return balanceTransactionRepository.findByAccountId(accountId,
				PageRequest.of(offset, limit, Sort.by("createdAt").descending()));

	}


	/**
	 * This method will create new balance transaction and set new balance to account
	 *
	 * @param accountId
	 * 		account id
	 * @param amount
	 * 		transfer amount
	 * @param balanceBefore
	 * 		balance amount before starting of transaction
	 * @param description
	 * 		description
	 * @param sourceType
	 * 		source is transaction type
	 * @param sourceId
	 * 		source id can be transfer, payment
	 * @return created balance object
	 */

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public BalanceTransaction createBalanceTransactionAndSetAccount(Long accountId,
			Long amount,
			Long balanceBefore,
			String description,
			TransactionType sourceType,
			Long sourceId) {

		Long balanceAfter = balanceBefore + amount;

		// Create new balance
		BalanceTransaction balanceTransaction = new BalanceTransaction();

		balanceTransaction.setAccountId(accountId);
		balanceTransaction.setAmount(amount);
		balanceTransaction.setBalanceAfter(balanceAfter);
		balanceTransaction.setDescription(description);
		balanceTransaction.setSourceType(sourceType);
		balanceTransaction.setSourceId(sourceId);

		BalanceTransaction createdBalanceTransaction = balanceTransactionRepository.save(balanceTransaction);

		// Set new balance to account
		accountRepository.updateAccountBalance(accountId, balanceAfter);

		return createdBalanceTransaction;


	}


}
