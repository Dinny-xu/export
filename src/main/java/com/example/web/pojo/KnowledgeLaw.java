package com.example.web.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class KnowledgeLaw implements Serializable {
    /**
     * ID主键
     */
    private String id;

    /**
     * 法律法规编号
     */
    private String number;

    /**
     * 法律法规名称
     */
    private String name;

    /**
     * 法律法规内容
     */
    private String content;

    /**
     * 法律法规类型
     */
    private String type;

    /**
     * 颁发时间
     */
    private String issuedTime;

    /**
     * 颁发单位名称
     */
    private String issuedBy;

    /**
     * 标签
     */
    private String tag;

    /**
     * 状态[未发布，审批中,已发布]
     */
    private String state;

    /**
     * 浏览量点击次数
     */
    private Integer viewCount;

    /**
     * 删除标记
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人ID
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人ID
     */
    private String updateUser;

    private static final long serialVersionUID = 1L;
}

