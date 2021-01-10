package api.util;

import api.db.model.BalanceTransaction;
import api.enums.TransactionType;
import api.response.BalanceTransactionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mehmet Aktas on 2020-12-12
 */

@ExtendWith(MockitoExtension.class)
class BalanceTransactionMapperTest {

	@InjectMocks
	private BalanceTransactionMapper balanceTransactionMapper = Mappers.getMapper(BalanceTransactionMapper.class);

	@Test
	void convertToBalanceTransactionDto_whenAccountIsGivenThenItShouldConvertToBalanceTransactionDto() {

		BalanceTransaction expected = new BalanceTransaction();

		expected.setId(11L);
		expected.setSourceId(100L);
		expected.setSourceType(TransactionType.TRANSFER);
		expected.setDescription("My test account");
		expected.setBalanceAfter(300L);
		expected.setAmount(200L);

		BalanceTransactionDto actual = balanceTransactionMapper.convertToBalanceTransactionDto(expected);

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);


	}
}
