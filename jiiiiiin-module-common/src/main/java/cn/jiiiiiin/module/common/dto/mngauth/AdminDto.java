package cn.jiiiiiin.module.common.dto.mngauth;

import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author jiiiiiin
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AdminDto extends Admin {

    private static final long serialVersionUID = -4730185793489105233L;
    private Long createTimestamp;

    private String createTimeStr;

    private String[] roleIds;

}
