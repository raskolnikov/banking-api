package api.response;

import api.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * Created by Mehmet Aktas on 2020-12-12
 */

@Getter
@Setter
public class BalanceTransactionDto {

	private Long id;
	private Long amount;
	private Long balanceAfter;
	private String description;
	private Long accountId;
	private TransactionType sourceType;
	private Long sourceId;
	private DateTime createdAt;
	private DateTime updatedAt;

}
