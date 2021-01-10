package api.db.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Mehmet Aktas on 2020-12-10
 */

@Entity
@Table(name = "transfers")
@Getter
@Setter
public class Transfer extends DbItem {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "amount", nullable = false)
	private Long amount;

	@Column(name = "description")
	private String description;

	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Column(name = "destination_account_id", nullable = false)
	private Long destinationAccountId;

}
