package spring.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "car")
@AllArgsConstructor
@NoArgsConstructor
public class Car extends BaseReferenceEntity {

	@EqualsAndHashCode.Include
	@GeneratedValue
	@Id
	private UUID id;

	@Column(unique = true, nullable = false)
	private String model;

	@Column(nullable = false)
	private Integer year;

	@ManyToOne
	@JoinColumn(name = "make_id", nullable = false)
	private Make make;

	@ManyToMany
	@JoinTable(name = "car_category", joinColumns = @JoinColumn(name = "car_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categorys = new HashSet<>();

}
