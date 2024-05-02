package hcmute.alohcmute.entities;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
@EqualsAndHashCode(exclude={"nhoms", "baiViets"})
@ToString (exclude = {"nhoms", "baiViets"})

@Entity
@Table
public class CheDo implements Serializable{
	private static final long serialVersionUID = -3486983368785225071L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaCheDo")
	private int maCheDo;
	
	@Column(name = "TenCheDo", columnDefinition = "nvarchar(2000)")
	private String tenCheDo;
	
	@OneToMany(mappedBy = "cheDoNhom", fetch = FetchType.LAZY)
	private Set<Nhom> nhoms;
	
	@OneToMany(mappedBy = "cheDoNhom", fetch = FetchType.LAZY)
	private Set<BaiViet> baiViets;
}
