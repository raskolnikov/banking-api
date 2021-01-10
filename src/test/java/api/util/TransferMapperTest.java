package api.util;

import api.db.model.Transfer;
import api.response.TransferDto;
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
class TransferMapperTest {

	@InjectMocks
	private TransferMapper transferMapper = Mappers.getMapper(TransferMapper.class);;

	@Test
	void convertToTransferDto_whenAccountIsGivenThenItShouldConvertToTransferDto() {

		Transfer expected = new Transfer();

		expected.setId(11L);
		expected.setAccountId(100L);
		expected.setDestinationAccountId(12L);
		expected.setDescription("My test description");
		expected.setAmount(300L);

		TransferDto actual = transferMapper.convertToTransferDto(expected);

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

	}
}