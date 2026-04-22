package spring.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "category")
public class Category extends BaseReferenceEntity {

	@GeneratedValue
	@Id
	private UUID id;

	@EqualsAndHashCode.Include
	@Column(nullable = false, unique = true)
	private String name;

	@ManyToMany(mappedBy = "categorys")
	private Set<Car> cars = new HashSet<>();
}
