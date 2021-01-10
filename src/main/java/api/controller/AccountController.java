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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mehmet Aktas on 2020-12-10
 * <p>
 * This API Controller will be responsible for accepting account CRUD or related operations
 */


@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	private final AccountService accountService;
	private final BalanceTransactionService balanceTransactionService;
	private final TransferService transferService;
	private final BalanceTransactionMapper balanceTransactionMapper;
	private final AccountMapper accountMapper;
	private final TransferMapper transferMapper;

	@Autowired
	public AccountController(AccountService accountService,
			BalanceTransactionService balanceTransactionService,
			TransferService transferService,
			BalanceTransactionMapper balanceTransactionMapper,
			AccountMapper accountMapper,
			TransferMapper transferMapper) {

		this.accountService = accountService;
		this.balanceTransactionService = balanceTransactionService;
		this.transferService = transferService;
		this.balanceTransactionMapper = balanceTransactionMapper;
		this.accountMapper = accountMapper;
		this.transferMapper = transferMapper;
	}

	/**
	 * This API method will create new account and set initial balance on to it.
	 *
	 * @param newAccountRequest
	 * 		new account details will be in here
	 * @return created account detail
	 */
	@PostMapping(value = "/", consumes = "application/json", produces = "application/json")
	public AccountDto createAccount(@Valid @RequestBody NewAccountRequest newAccountRequest) {

		LOGGER.debug("New account creation request is received for customer: " + newAccountRequest.getCustomerId());

		Account account = accountService.createAccount(newAccountRequest);

		LOGGER.debug("New account created for customer: " + newAccountRequest.getCustomerId());

		return accountMapper.convertToAccountDto(account);

	}

	/**
	 * This API method will retrieve account detail for given @param id
	 * <p>
	 * Account object will have  latest balance in it
	 *
	 * @param id
	 * 		account id
	 * @return account details
	 */
	@GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public AccountDto getAccount(@PathVariable("id") long id) {

		Account account = accountService.getAccount(id);

		return accountMapper.convertToAccountDto(account);

	}

	/**
	 * This API method will return balance transactions for given account.
	 * <p>
	 * Balance history can be seen here
	 *
	 * @param id
	 * 		account detail
	 * @param offset
	 * 		starting point of list
	 * @param limit
	 * 		size of returning list
	 * @return balance transactions
	 */
	@GetMapping(value = "/{id}/balance-transactions", consumes = "application/json", produces = "application/json")
	public List<BalanceTransactionDto> getAccountBalanceTransactions(@PathVariable("id") long id,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(value = "limit", defaultValue = "25") Integer limit) {

		// Precondition validation for account
		Account account = accountService.getAccount(id);

		List<BalanceTransaction> balanceTransactionList = balanceTransactionService.getAccountBalanceTransactions(id,
				offset,
				limit);

		return balanceTransactionList.stream()
				.map(balanceTransactionMapper::convertToBalanceTransactionDto)
				.collect(Collectors.toList());

	}

	/**
	 * This API method will return transfers for given account.
	 * <p>
	 * Returning list will contain in and out transfers
	 *
	 * @param id
	 * 		account id
	 * @param offset
	 * 		starting point of list
	 * @param limit
	 * 		size of returning list
	 * @return transfer list
	 */
	@GetMapping(value = "/{id}/transfers", consumes = "application/json", produces = "application/json")
	public List<TransferDto> getAccountTransfers(@PathVariable("id") long id,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(value = "limit", defaultValue = "25") Integer limit) {

		// Precondition validation for account
		Account account = accountService.getAccount(id);

		List<Transfer> transferList = transferService.getAccountTransfers(id, offset, limit);

		return transferList.parallelStream().map(transferMapper::convertToTransferDto).collect(Collectors.toList());
	}


}
