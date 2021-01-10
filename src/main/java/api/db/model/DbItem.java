package api.db.model;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * Created by Mehmet Aktas on 2020-12-10
 */

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class DbItem {


	@Column(name = "created_at", nullable = false)
	@CreatedDate
	private DateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@LastModifiedDate
	private DateTime updatedAt;

}
