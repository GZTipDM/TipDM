package com.tipdm.framework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tipdm.framework.common.hibernate.datatype.JsonType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.type.TextType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 统一定义id的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * 
 * @author yarn
 */
// JPA 基类的标识
@MappedSuperclass
@TypeDefs( {@TypeDef( name= "json", typeClass = JsonType.class), @TypeDef( name= "text", typeClass = TextType.class)})
public abstract class IdEntity<PK extends Serializable> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createTime = Calendar.getInstance().getTime();

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Column(name = "creator_id")
	private Long creatorId;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Column(name = "creator_name")
	private String creatorName;

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	@Override
	public boolean equals(Object obj) {

		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!getClass().equals(obj.getClass())) {
			return false;
		}

		IdEntity<?> that = (IdEntity<?>) obj;
		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {

		int hashCode = 17;

		hashCode += null == getId() ? 0 : getId().hashCode() * 31;

		return hashCode;
	}

	// 通过反射实现toString apache common lang包里
//	@Override
//	public String toString() {
//		return ReflectionToStringBuilder.toString(this);
//	}
}
