package api.service;

import api.db.model.Account;
import api.db.model.Customer;
import api.db.repository.AccountRepository;
import api.enums.TransactionType;
import api.exception.NotFoundException;
import api.request.NewAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Mehmet Aktas on 2020-12-10
 * <p>
 * This service class will be doing creation accounts and retrieving account detail.
 */

@Service
public class AccountService {

	private final AccountRepository accountRepository;
	private final BalanceTransactionService balanceTransactionService;

	@Autowired
	public AccountService(AccountRepository accountRepository, BalanceTransactionService balanceTransactionService) {

		this.accountRepository = accountRepository;
		this.balanceTransactionService = balanceTransactionService;
	}

	/**
	 * This method will create new account and set initial balance in it.
	 *
	 * @param newAccountRequest
	 * 		new account details will be in here
	 * @return created account detail
	 */
	//@Transactional
	public Account createAccount(NewAccountRequest newAccountRequest) {

		Account account = new Account();

		Customer customer = new Customer();
		customer.setId(newAccountRequest.getCustomerId());

		account.setCustomer(customer);
		account.setName(newAccountRequest.getAccountName());
		account.setBalance(newAccountRequest.getInitialBalance());

		Account createdAccount = accountRepository.save(account);

		// Deposit the amount to existing account
		balanceTransactionService.createBalanceTransactionAndSetAccount(createdAccount.getId(),
				newAccountRequest.getInitialBalance(),
				0L,
				"Initial deposit",
				TransactionType.DEPOSIT,
				null);

		return createdAccount;

	}

	/**
	 * This method will return account details for given id
	 *
	 * @param id
	 * 		account id
	 * @return account object will return
	 */
	public Account getAccount(Long id) {

		return accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found", id));

	}


}
