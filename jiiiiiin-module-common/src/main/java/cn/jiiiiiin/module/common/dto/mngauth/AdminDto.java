package cn.jiiiiiin.module.common.dto.mngauth;

import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AdminDto extends Admin {

    private Long createTimestamp;

    private String createTimeStr;

    private String[] roleIds;

}
