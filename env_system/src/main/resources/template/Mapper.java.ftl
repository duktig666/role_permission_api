package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
import org.springframework.stereotype.Component;

/**
* <p>
* ${table.comment!} Mapper 接口
* </p>
*
* @author RenShiWei
* @since 2020-08-06
*/
@Component
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}