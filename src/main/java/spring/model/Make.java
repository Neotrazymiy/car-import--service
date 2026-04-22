package spring.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "make")
public class Make extends BaseReferenceEntity {

	@GeneratedValue
	@Id
	private UUID id;

	@Column(nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "make")
	private List<Car> cars = new ArrayList<>();
}
