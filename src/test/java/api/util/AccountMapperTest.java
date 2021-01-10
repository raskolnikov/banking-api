package api.util;

import api.db.model.Account;
import api.db.model.Customer;
import api.response.AccountDto;
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
class AccountMapperTest {

	@InjectMocks
	private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

	@Test
	void convertToAccountDto_whenAccountIsGivenThenItShouldConvertToAccountDto() {

		Account expected = new Account();

		expected.setId(11L);
		expected.setBalance(100L);

		Customer customer = new Customer();
		customer.setId(12L);
		expected.setCustomer(customer);
		expected.setName("My test account");

		AccountDto actual = accountMapper.convertToAccountDto(expected);

		assertThat(actual).usingRecursiveComparison().ignoringFields("customerId").isEqualTo(expected);


	}
}