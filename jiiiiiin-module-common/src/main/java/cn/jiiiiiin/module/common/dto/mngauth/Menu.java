package cn.jiiiiiin.module.common.dto.mngauth;

import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.io.Serializable;
import java.util.List;

/**
 * 前端所需菜单
 *
 * @author jiiiiiin
 */
@Data
@Accessors(chain = true)
public class Menu implements Serializable {

    private static final long serialVersionUID = -7510473557351395479L;

    public static ModelMapper MODEL_MAPPER = new ModelMapper();

    static {
        MODEL_MAPPER.addMappings(new PropertyMap<Resource, Menu>() {

            @Override
            protected void configure() {
                map().setPath(source.getUrl());
                map().setTitle(source.getName());
            }
        });
    }

    // TODO 补充 ApiModelProperty
    private String path;
    private String title;
    private String icon;
    private List<Menu> children;
    @ApiModelProperty(value = "菜单排序号")
    private Integer num;

}
