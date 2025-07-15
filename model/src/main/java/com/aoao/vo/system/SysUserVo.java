//
//
package com.aoao.vo.system;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户查询实体
 * </p>
 */
@Data
public class SysUserVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String password;

	private String openId;

	private String name;

	private String phone;

	private String headUrl;

	private String postName;

	private String postId;

	private String deptName;

	private String deptId;

	private String description;

	private Integer status;

	private Date createTime;

	private Date updateTime;

}

