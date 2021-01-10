package api.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * Created by Mehmet Aktas on 2020-12-11
 */

@Getter
@Setter
@EqualsAndHashCode
public class TransferDto {

	private Long id;
	private Long amount;
	private String description;
	private Long accountId;
	private Long destinationAccountId;
	private DateTime createdAt;
	private DateTime updatedAt;

}
