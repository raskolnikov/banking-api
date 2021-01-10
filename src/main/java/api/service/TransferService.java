package api.service;

import api.db.model.Account;
import api.db.model.Transfer;
import api.db.repository.AccountRepository;
import api.db.repository.TransferRepository;
import api.enums.TransactionType;
import api.exception.InsufficientBalanceException;
import api.exception.InvalidRequestParamException;
import api.exception.NotFoundException;
import api.request.NewTransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Mehmet Aktas on 2020-12-10
 * <p>
 * This service class will be doing creation transfers and retrieving transfers.
 */

@Service
public class TransferService {

	private final AccountRepository accountRepository;
	private final TransferRepository transferRepository;
	private final BalanceTransactionService balanceTransactionService;

	@Autowired
	public TransferService(AccountRepository accountRepository,
			TransferRepository transferRepository,
			BalanceTransactionService balanceTransactionService) {

		this.accountRepository = accountRepository;

		this.transferRepository = transferRepository;
		this.balanceTransactionService = balanceTransactionService;
	}

	/**
	 * This method will create transfer between two accounts and set new balance onto two accounts
	 * <p>
	 * Operations will be in transaction scope with Serializable scope to avoid race condition happens
	 *
	 * @param newTransferRequest
	 * 		new transfer detail be kept in this object
	 * @return new created  transfer
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Transfer createTransfer(NewTransferRequest newTransferRequest) {

		if (newTransferRequest.getAmount() < 1) {

			throw new InvalidRequestParamException("Transfer amount has to be at least 1 pence");
		}

		if (newTransferRequest.getAccountId().equals(newTransferRequest.getDestinationAccountId())) {

			throw new InvalidRequestParamException("You can not make transfer between same accounts");
		}

		Account sourceAccount = accountRepository.findById(newTransferRequest.getAccountId())
				.orElseThrow(() -> new NotFoundException("Account not found", newTransferRequest.getAccountId()));

		if (sourceAccount.getBalance() < newTransferRequest.getAmount()) {

			throw new InsufficientBalanceException("There is no enough balance in account");

		}

		Account destinationAccount = accountRepository.findById(newTransferRequest.getDestinationAccountId())
				.orElseThrow(() -> new NotFoundException("Destination account not found",
						newTransferRequest.getDestinationAccountId()));


		// Create transfer object first
		Transfer transfer = new Transfer();

		transfer.setAccountId(sourceAccount.getId());
		transfer.setAmount(newTransferRequest.getAmount());
		transfer.setDescription(newTransferRequest.getDescription());
		transfer.setDestinationAccountId(newTransferRequest.getDestinationAccountId());

		Transfer createdTransfer = transferRepository.save(transfer);

		// Balance adjustment for source account
		balanceTransactionService.createBalanceTransactionAndSetAccount(sourceAccount.getId(),
				-1 * newTransferRequest.getAmount(),
				sourceAccount.getBalance(),
				newTransferRequest.getDescription(),
				TransactionType.TRANSFER,
				createdTransfer.getId());

		// Balance adjustment for destination account
		balanceTransactionService.createBalanceTransactionAndSetAccount(destinationAccount.getId(),
				newTransferRequest.getAmount(),
				destinationAccount.getBalance(),
				newTransferRequest.getDescription(),
				TransactionType.TRANSFER,
				createdTransfer.getId());


		return createdTransfer;
	}


	/**
	 * This  method will retrieve transfers for given filtering params.
	 * <p>
	 * Returning transfer list will contain all in and out transfers
	 *
	 * @param accountId
	 * 		account id
	 * @param offset
	 * 		starting point of list
	 * @param limit
	 * 		size of returning list
	 * @return transfer list
	 */
	public List<Transfer> getAccountTransfers(long accountId, Integer offset, Integer limit) {

		return transferRepository.findInOutTransfersByAccountId(accountId,
				PageRequest.of(offset, limit, Sort.by("createdAt").descending()));

	}


}
