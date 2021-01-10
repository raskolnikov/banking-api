package api.controller;

import api.db.model.Transfer;
import api.request.NewTransferRequest;
import api.response.TransferDto;
import api.service.TransferService;
import api.util.TransferMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Mehmet Aktas on 2020-12-11
 * <p>
 * This API Controller will be responsible for accepting transfer CRUD or related operations
 */


@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

	private final TransferService transferService;
	private final TransferMapper transferMapper;

	@Autowired
	public TransferController(TransferService transferService, TransferMapper transferMapper) {

		this.transferService = transferService;
		this.transferMapper = transferMapper;
	}


	/**
	 * Transfer creation requests call be done to this method.
	 * This will create transfer and set new account balance and return transfer DTO object
	 *
	 * @param newTransferRequest
	 * 		new transfer detail be kept in this object
	 * @return new created  transfer
	 */
	@PostMapping(value = "/", consumes = "application/json", produces = "application/json")
	public TransferDto createTransfer(@Valid @RequestBody NewTransferRequest newTransferRequest) {

		LOGGER.debug("New transfer request is received from account: "
				+ newTransferRequest.getAccountId()
				+ " to: "
				+ newTransferRequest.getDestinationAccountId());

		Transfer transfer = transferService.createTransfer(newTransferRequest);

		LOGGER.debug("New transfer is done from account: "
				+ newTransferRequest.getAccountId()
				+ " to: "
				+ newTransferRequest.getDestinationAccountId());

		return transferMapper.convertToTransferDto(transfer);

	}

}
